#!/usr/bin/env python3

#  Copyright 2020 Google LLC
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#

import argparse
import csv
import json
import os
import sys
from datetime import datetime, timedelta
from pathlib import Path

csv_columns_to_json_keys_movie = {"id": "@id", "name": "name", "description": "description", "uri": "url", "duration": "duration", "EIDR": "titleEIDR"}
csv_columns_to_json_keys_episode = {"id": "@id", "name": "name", "description": "description", "uri": "url", "episodeNumber": "episodeNumber", "duration": "duration", "EIDR": "titleEIDR"}
context_json_key = "@context"
context_value_media = ["http://schema.org", {"@language": "en"}]
context_value = "http://schema.org"
id_json_key = "@id"
type_json_key = "@type"
type_json_value = "DataFeed"
dateModified_json_key = "dateModified"
dateModified_json_value = datetime.now().replace(microsecond=0).isoformat()+"Z"
dataFeed_json_key = "dataFeedElement"
partOfSeries_json_key = "partOfSeries"
partOfSeason_json_key = "partOfSeason"
tvSeriesUri_csv_key = "TVSeriesUri"
tvSeasonUri_csv_key = "TVSeasonUri"
seasonNumber_csv_key = "seasonNumber"
category_csv_key = "category"
potential_action_key = "potentialAction"
csv_type_to_media_action_type = {"clip" : "Clip", "episode": "TVEpisode", "movie": "Movie"}

def main():
  # Parse the output file argument
  arg_parser = argparse.ArgumentParser()
  arg_parser.add_argument("input_file", help="CSV file to read from")
  arg_parser.add_argument("output_movie_file", help="File to output media actions feed for movies in json such as movies.json")
  arg_parser.add_argument("output_episodes_file", help="File to output media actions feed for TV episodes in json such as episodes.json")
  args = arg_parser.parse_args()

  # Avoid overwriting an existing movies file
  if Path(args.output_movie_file).exists():
    sys.exit(args.output_movie_file + " already exists; aborting")

  # Avoid overwriting an existing episodes file
  if Path(args.output_episodes_file).exists():
    sys.exit(args.output_episodes_file + " already exists; aborting")

  # Read in the csv and process it
  try:
    with open(args.input_file) as csv_file, open(args.output_movie_file, 'w') as movie_json_file, open(args.output_episodes_file,'w') as episodes_json_file:
      # Read each row and convert to JSON
      dict_reader = csv.DictReader(csv_file)
      csv_content_list = list(dict_reader)

      # Create content list for movies for the media actions feed
      json_movies_list = create_json_movies_list(csv_content_list)

      # Generate wrapper with content for movies
      movies_dict = {context_json_key: context_value, type_json_key: type_json_value,
                    dateModified_json_key: dateModified_json_value, dataFeed_json_key: json_movies_list}
      json.dump(obj=movies_dict, fp=movie_json_file, indent=4)

      # get list of unique TV series
      csv_tv_series_tuple_list = get_unique_tv_series(csv_content_list)
      # Create content list for TV Series for the media actions feed
      json_tv_series_list = create_tv_series_list(csv_tv_series_tuple_list)

      # Create content list for TV Episodes for the media actions feed
      json_episodes_list = create_json_episodes_list(csv_content_list)

      # Generate wrapper with content for TV episodes
      episodes_dict = {context_json_key: context_value, type_json_key: type_json_value,
                     dateModified_json_key: dateModified_json_value, dataFeed_json_key: json_tv_series_list + json_episodes_list}
      json.dump(obj=episodes_dict, fp=episodes_json_file, indent=4)

  except FileNotFoundError as err:
    sys.exit("File doesn't exist: {0}".format(err))
  except PermissionError as err:
    sys.exit("Permission error, failed to read file: (0)".format(err))
  except KeyError as err:
    # Remove the output files since they weren't written to successfully
    os.remove(args.output_movie_file)
    os.remove(args.output_episodes_file)
    sys.exit("Missing key in input CSV: {0}".format(err))

  print("Movies media action feed JSON written to " + args.output_movie_file)
  print("TV episodes media action feed JSON written to " + args.output_episodes_file)

def create_json_movies_list(csv_content_list):
  '''Takes a dict from the CSV input and returns a dict for the movies media feed output'''
  json_movies_list = []
  for content in csv_content_list:
    if csv_type_to_media_action_type[content["type"]] != "Movie":
      continue
    movie_item = {}
    movie_item[context_json_key] = context_value_media
    movie_item[type_json_key] = csv_type_to_media_action_type[content["type"]]

    for csv_key, json_key in csv_columns_to_json_keys_movie.items():
      movie_item[json_key] = content[csv_key]

    movie_item[potential_action_key] = create_potentialAction_object(content["uri"])

    json_movies_list.append(movie_item)
  return json_movies_list

def create_json_episodes_list(csv_content_list):
  '''Takes a dict from the CSV input and returns a dict for the movies media feed output'''
  json_episodes_list = []
  for content in csv_content_list:
    if csv_type_to_media_action_type[content["type"]] != "TVEpisode":
      continue
    episode_item = {}
    episode_item[context_json_key] = context_value_media
    episode_item[type_json_key] = csv_type_to_media_action_type[content["type"]]

    for csv_key, json_key in csv_columns_to_json_keys_episode.items():
      episode_item[json_key] = content[csv_key]

    episode_item[partOfSeason_json_key] = create_partOfSeason_object(content)
    episode_item[partOfSeries_json_key] = create_partOfSeries_object(content)
    episode_item[potential_action_key] = create_potentialAction_object(content["uri"])

    json_episodes_list.append(episode_item)
  return json_episodes_list

def create_tv_series_list(tv_series_tuple_list):
  tv_series_list = []
  for series_tuple in tv_series_tuple_list:
    series_item = {}
    series_item[context_json_key] = context_value_media
    series_item[type_json_key] = "TVSeries"
    series_item[id_json_key] = series_tuple[tvSeriesUri_csv_key]
    series_item["url"] = series_tuple[tvSeriesUri_csv_key]
    series_item["name"] = series_tuple[category_csv_key]
    series_item[potential_action_key] = \
      create_potentialAction_object(series_tuple[tvSeriesUri_csv_key])
    tv_series_list.append(series_item)
  return tv_series_list

def create_partOfSeason_object(csv_content):
  partOfSeason = {}
  partOfSeason[type_json_key] = "TVSeason"
  partOfSeason[id_json_key] = csv_content[tvSeasonUri_csv_key]
  partOfSeason["seasonNumber"] = csv_content[seasonNumber_csv_key]
  return partOfSeason

def create_partOfSeries_object(csv_content):
  partOfSeries = {}
  partOfSeries[type_json_key] = "TVSeries"
  partOfSeries[id_json_key] = csv_content[tvSeriesUri_csv_key]
  partOfSeries["name"] = csv_content[category_csv_key]
  return partOfSeries

def create_potentialAction_object(csv_content_uri):
  potentialAction = {}
  potentialAction[type_json_key] = "WatchAction"

  target = {}
  target[type_json_key] = "EntryPoint"
  target["urlTemplate"] = csv_content_uri
  target["actionPlatform"] = ["http://schema.org/AndroidTVPlatform",
                              "http://schema.googleapis.com/GoogleVideoCast"]
  potentialAction["target"] = target

  accessibility_requirement = {}
  accessibility_requirement[type_json_key] = "ActionAccessSpecification"
  accessibility_requirement[category_csv_key] = "nologinrequired"
  accessibility_requirement["availabilityStarts"] = datetime.now().replace(microsecond=0) \
                                                    .isoformat()+"Z"

  """
  Since the content is always available after it's published, the availability end date property is
  set to be 20 years from the current date. For further details on the properties, visit the link -
  https://developers.google.com/actions/media/reference/data-specification/watch-actions-common-specification.
  """
  accessibility_requirement["availabilityEnds"] = (datetime.now() + timedelta(days=(20*365))) \
                                                  .replace(microsecond=0).isoformat()+"Z"
  accessibility_requirement["eligibleRegion"] = "EARTH"
  potentialAction["actionAccessibilityRequirement"] = accessibility_requirement

  return potentialAction

def get_unique_tv_series(csv_content_list):
  unique_tv_series = []
  for content in csv_content_list:
    if csv_type_to_media_action_type[content["type"]] != "TVEpisode":
      continue
    tv_series = {tvSeriesUri_csv_key: content[tvSeriesUri_csv_key], category_csv_key: content[category_csv_key]}
    if tv_series not in unique_tv_series:
      unique_tv_series.append(tv_series)
  return unique_tv_series

if __name__ == "__main__":
  main()
