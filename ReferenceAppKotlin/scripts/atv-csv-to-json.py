#!/usr/bin/env python3
import argparse
import csv
import json
import os
import sys
from datetime import datetime
from pathlib import Path

csv_columns_to_json_keys = {"id": "id", "name": "name", "description": "description", "uri": "uri", "video": "videoUri", "thumbnail": "thumbnailUri", "background": "backgroundUri",
                            "category": "category", "duration": "duration", "TVSeriesUri": "seriesUri", "episodeNumber": "episodeNumber","type": "videoType",
                            "seasonNumber": "seasonNumber", "TVSeasonUri": "seasonUri"}
api_json_content_key = "content"
api_json_metadata_key = "metadata"
metadata_last_updated_key = "last_updated"

def main():
    # Parse the output file argument
    arg_parser = argparse.ArgumentParser()
    arg_parser.add_argument("input_file", help="CSV file to read from")
    arg_parser.add_argument("output_file", help="File to output json to such as api.json")
    args = arg_parser.parse_args()

    # Avoid overwriting an existing file
    if Path(args.output_file).exists():
        sys.exit(args.output_file + " already exists; aborting")

    # Read in the csv and process it
    try:
        with open(args.input_file) as csv_file, open(args.output_file, 'w') as json_api_file:
            # Read each row and convert to JSON
            dict_reader = csv.DictReader(csv_file)
            csv_content_list = list(dict_reader)

            # Create content list for the JSON API output
            json_content_list = create_json_api_content_list(csv_content_list)

            # Generate wrapper with content
            final_dict = {api_json_content_key: json_content_list, api_json_metadata_key: get_api_metadata_dict()}
            json.dump(obj=final_dict, fp=json_api_file, indent=4)
    except FileNotFoundError as err:
        sys.exit("File doesn't exist: {0}".format(err))
    except PermissionError as err:
        sys.exit("Permission error, failed to read file: (0)".format(err))
    except KeyError as err:
        # Remove the output file since it wasn't written to successfully
        os.remove(args.output_file)
        sys.exit("Missing key in input CSV: {0}".format(err))

    print("JSON written to " + args.output_file)

def get_api_metadata_dict():
    '''Returns a dictionary with metadata for the API JSON'''
    return {metadata_last_updated_key: datetime.now().replace(microsecond=0).isoformat()}

def create_json_api_content_list(csv_content_list):
    '''Takes a dict from the CSV input and returns a dict for the JSON API output'''
    json_api_list = []
    for content in csv_content_list:
        json_content_item = {}
        for csv_key, json_key in csv_columns_to_json_keys.items():
            json_content_item[json_key] = content[csv_key]
        json_api_list.append(json_content_item)
    return json_api_list

if __name__ == "__main__":
    main()
