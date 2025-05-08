import json
import random
import re
from datetime import datetime
from cohere import ClientV2
from dataclasses import dataclass
import csv
from typing import List
import time
from dotenv import load_dotenv
import os


@dataclass
class WordRequest:
    word: str
    language: str


@dataclass
class WordInfo:
    word: str
    language: str
    translation: str
    description: str
    etymology: str
    exampleSentence: str
    partOfSpeech: str
    phoneticSpelling: str


def get_data(word: str, language: str, api_key: str) -> str:
    client = ClientV2(api_key=api_key)
    response = client.chat(
        model="command-a-03-2025",
        messages=[
            {
                "role": "system",
                "content": "You are a helpful language assistant.\n\nI will give you a word and a language.\n\nReturn the following fields in JSON:\n- translation (in English)\n- description (meaning in plain English)\n- etymology (brief origin)\n- exampleSentence (in the original language)\n- partOfSpeech (noun, verb, etc.)\n- phoneticSpelling (IPA if possible)"
            },
            {
                "role": "user",
                "content": [
                    {
                        "type": "text",
                        "text": word + ", " + language
                    }
                ]
            },
        ],
        temperature=0.3
    )
    return response.message.content[0].text


def parse_json(word: str, language: str, json_str: str) -> WordInfo:
    match = re.search(r'\{.*?\}', json_str, re.DOTALL)
    if match:
        json_string = match.group()

        data = json.loads(json_string)

        return WordInfo(
            word=word,
            language=language,
            translation=data['translation'],
            description=data['description'],
            etymology=data['etymology'],
            exampleSentence=data['exampleSentence'],
            partOfSpeech=data['partOfSpeech'],
            phoneticSpelling=data['phoneticSpelling']
        )
    else:
        raise RuntimeError("Could not parse: " + word + "\n json: " + json_str)


def read_word_requests_from_csv(filename: str = "input.csv") -> List[WordRequest]:
    requests = []
    with open(filename, newline='', encoding='utf-8') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            word = row['word'].strip()
            language = row['language'].strip()
            requests.append(WordRequest(word, language))
    return requests


def write_word_info_to_csv(word_infos: List[WordInfo]):
    timestamp = datetime.now().strftime("%Y-%m-%d_%H-%M-%S")
    filename = f"output_{timestamp}.csv"

    with open(filename, mode='w', newline='', encoding='utf-8') as csvfile:
        fieldnames = [
            'word',
            'language',
            'translation',
            'description',
            'etymology',
            'exampleSentence',
            'partOfSpeech',
            'phoneticSpelling'
        ]
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
        writer.writeheader()

        for info in word_infos:
            writer.writerow({
                'word': info.word,
                'language': info.language,
                'translation': info.translation,
                'description': info.description,
                'etymology': info.etymology,
                'exampleSentence': info.exampleSentence,
                'partOfSpeech': info.partOfSpeech,
                'phoneticSpelling': info.phoneticSpelling
            })


def do_work(info: WordRequest, api_key: str, parsed_list, start_time):
    json_str = get_data(info.word, info.language, api_key)
    parsed = parse_json(info.word, info.language, json_str)
    parsed_list.append(parsed)
    print("Parsed: " + parsed.word + "\nTime: " + str(time.time() - start_time))


def main():
    start_time = time.time()

    load_dotenv()

    api_keys_raw = os.getenv("API_KEYS")
    random_order = os.getenv("RANDOM_ORDER", "False").lower() == "true"
    input_file_name = os.getenv("INPUT_FILE_NAME", "input.csv")
    api_keys = [key.strip() for key in api_keys_raw.split(",") if key]

    to_convert = read_word_requests_from_csv(input_file_name)

    deleted_keys = []
    parsed_list = []
    key = 0

    for info in to_convert:
        start_time_i = time.time()
        try:
            do_work(info, api_keys[key], parsed_list, start_time_i)
        except Exception as e:
            if "You are using a Trial key, which is limited to 1000 API calls / month" in str(e):
                removed = api_keys.pop(key)
                key -= 1
                deleted_keys.append(removed)
                try:
                    do_work(info, api_keys[key], parsed_list, start_time_i)
                except Exception as e:
                    print(f"Caught an exception: {e}\nWord skipped: {info.word}")
            else:
                print(f"Caught an exception: {e}\nWord skipped: {info.word}")

        finally:
            key += 1
            key %= len(api_keys)
            delay = 6 - (time.time() - start_time_i)
            if delay > 0:
                print(delay)
                time.sleep(delay)

    if random_order:
        random.shuffle(parsed_list)

    write_word_info_to_csv(parsed_list)
    print("Finished in {} seconds".format(time.time() - start_time))
    print("Total words: {}".format(len(to_convert)))
    print("Total words parsed: {}".format(len(parsed_list)))
    print("Percentage lost: {}".format((len(to_convert) - len(parsed_list)) / len(to_convert) * 100))
    print("Keys deleted: " + str(deleted_keys))


if __name__ == "__main__":
    main()
