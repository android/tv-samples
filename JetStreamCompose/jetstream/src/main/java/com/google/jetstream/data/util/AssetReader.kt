/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.jetstream.data.util // ktlint-disable filename

import android.content.Context
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AssetsReader @Inject constructor(
    @ApplicationContext private val context: Context,
    val gson: Gson = Gson()
) {
    fun getJsonDataFromAsset(context: Context = this.context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }

    /**
     * Deserializes the given json file into the inferred return type.
     * @param fileName Name (with extension) of the json file to be deserialized. For example if
     * the json file's name is 'file1.json', then we call this method as follows:
     * ###readJsonFile("file1.json")
     * @return Deserialized object of the inferred type.
     */
    inline fun <reified T> readJsonFile(fileName: String): T? {
        val json = getJsonDataFromAsset(fileName = fileName)
        return gson.fromJson(json, T::class.java)
    }

}
