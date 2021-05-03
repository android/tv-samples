/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.tv.reference.homescreenchannels

import java.util.Collections

/**
 * Data structure for tracking sets of program IDs that are browsable and not browsable
 */
class ProgramIdsInChannel {

    private val browsableProgramIds = HashSet<String>()
    private val nonBrowsableProgramIds = HashSet<String>()

    /**
     * Adds the [programId] to one of the setts based on [isBrowsable]
     *
     * Passing a [programId] more than once with different [isBrowsable] values means it will exist
     * in both sets
     */
    fun addProgramId(programId: String, isBrowsable: Boolean) {
        if (isBrowsable) {
            browsableProgramIds.add(programId)
        } else {
            nonBrowsableProgramIds.add(programId)
        }
    }

    /**
     * Returns the number of browsable programs useful for knowing how many programs a user sees
     */
    fun getBrowsableProgramCount(): Int {
        return browsableProgramIds.size
    }

    /**
     * Returns an unmodifiable set of browsable program IDs
     */
    fun getBrowsableProgramIds(): Set<String> {
        return Collections.unmodifiableSet(browsableProgramIds)
    }

    /**
     * Returns an unmodifiable set of non-browsable program IDs
     */
    fun getNonBrowsableProgramIds(): Set<String> {
        return Collections.unmodifiableSet(nonBrowsableProgramIds)
    }
}
