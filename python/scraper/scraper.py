import json
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
        raise RuntimeError("Could not parse")


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


def main():
    to_convert = read_word_requests_from_csv()

    load_dotenv()

    api_key = os.getenv("API_KEY")

    parsed_list = []
    for info in to_convert:
        try:
            json_str = get_data(info.word, info.language, api_key)
            parsed = parse_json(info.word, info.language, json_str)
            parsed_list.append(parsed)
        except Exception as e:
            print("Caught an exception:", e)

    write_word_info_to_csv(parsed_list)
    time.sleep(30)


if __name__ == "__main__":
    main()
