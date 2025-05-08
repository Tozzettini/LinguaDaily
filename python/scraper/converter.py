import csv


def convert_csv_with_language(input_file, output_file, language):
    with open(input_file, mode='r', newline='', encoding='utf-8') as infile, \
            open(output_file, mode='w', newline='', encoding='utf-8') as outfile:

        reader = csv.reader(infile)
        writer = csv.writer(outfile)

        writer.writerow(['word', 'language'])

        for i, row in enumerate(reader):
            if not row:
                continue
            word = row[0].strip()
            if i == 0 and word.lower() == 'word':
                continue
            writer.writerow([word, language])


def main():
    convert_csv_with_language("cleaned_words.csv", "input2.csv", "Dutch")


if __name__ == '__main__':
    main()
