import pandas as pd
import sys
import os

def filter_duplicates(csv_path, case_insensitive=True):
    try:
        # Load CSV
        df = pd.read_csv(csv_path)

        if 'word' not in df.columns:
            print("Error: 'word' column not found.")
            return

        # Remove duplicates based on 'word' column
        if case_insensitive:
            # Normalize for comparison
            df["_normalized_word"] = df["word"].str.lower()
            df_filtered = df.drop_duplicates(subset="_normalized_word", keep="first")
            df_filtered = df_filtered.drop(columns=["_normalized_word"])
        else:
            df_filtered = df.drop_duplicates(subset="word", keep="first")

        # Save to new file
        base, ext = os.path.splitext(csv_path)
        output_path = f"{base}_filtered{ext}"
        df_filtered.to_csv(output_path, index=False)

        print(f"Filtered CSV (unique words only) saved to: {output_path}")

    except Exception as e:
        print(f"An error occurred: {e}")


if __name__ == "__main__":
    filter_duplicates("english_converted.csv")
