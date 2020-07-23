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

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ProgramIdsInChannelTest {

    private lateinit var programIdsInChannel: ProgramIdsInChannel

    @Before
    fun setUp() {
        programIdsInChannel = ProgramIdsInChannel()
    }

    @Test
    fun getBrowsableProgramCount() {
        // A new object should have no content
        assertThat(programIdsInChannel.getBrowsableProgramIds()).isEmpty()

        // Add two browsable IDs
        programIdsInChannel.addProgramId(programId = "123", isBrowsable = true)
        programIdsInChannel.addProgramId(programId = "456", isBrowsable = true)
        assertThat(programIdsInChannel.getBrowsableProgramIds()).hasSize(2)

        // Re-add an existing ID and verify the count is the same
        programIdsInChannel.addProgramId(programId = "123", isBrowsable = true)
        assertThat(programIdsInChannel.getBrowsableProgramIds()).hasSize(2)

        // Add non-browsable IDs and verify the count doesn't change
        programIdsInChannel.addProgramId(programId = "789", isBrowsable = false)
        programIdsInChannel.addProgramId(programId = "abc", isBrowsable = false)
        assertThat(programIdsInChannel.getBrowsableProgramIds()).hasSize(2)
    }

    @Test
    fun getBrowsableProgramIds() {
        // A new object should have no content
        assertThat(programIdsInChannel.getBrowsableProgramIds()).isEmpty()

        // Create a Set of browsable IDs
        val browsableIds = HashSet<String>().apply {
            add("123")
            add("456")
        }

        // Add them
        browsableIds.forEach {
            programIdsInChannel.addProgramId(programId = it, isBrowsable = true)
        }

        // The Set of browsable content should now match
        assertThat(programIdsInChannel.getBrowsableProgramIds())
            .containsExactlyElementsIn(browsableIds)

        // There shouldn't be any non-browsable content
        assertThat(programIdsInChannel.getNonBrowsableProgramIds()).isEmpty()
    }

    @Test
    fun getNonBrowsableProgramIds() {
        // A new object should have no content
        assertThat(programIdsInChannel.getNonBrowsableProgramIds()).isEmpty()

        // Create a Set of browsable IDs
        val nonBrowsableIds = HashSet<String>().apply {
            add("123")
            add("456")
        }

        // Add them
        nonBrowsableIds.forEach {
            programIdsInChannel.addProgramId(it, false)
        }

        // The Set of non-browsable content should now match
        assertThat(programIdsInChannel.getNonBrowsableProgramIds())
            .containsExactlyElementsIn(nonBrowsableIds)

        // There shouldn't be any browsable content
        assertThat(programIdsInChannel.getBrowsableProgramIds()).isEmpty()
    }
}
