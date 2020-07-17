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

import org.junit.Assert
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
        Assert.assertEquals(0, programIdsInChannel.getBrowsableProgramCount())

        // Add two browsable IDs
        programIdsInChannel.addProgramId("123", true)
        programIdsInChannel.addProgramId("456", true)
        Assert.assertEquals(2, programIdsInChannel.getBrowsableProgramCount())

        // Re-add an existing ID and verify the count is the same
        programIdsInChannel.addProgramId("123", true)
        Assert.assertEquals(2, programIdsInChannel.getBrowsableProgramCount())

        // Add non-browsable IDs and verify the count doesn't change
        programIdsInChannel.addProgramId("789", false)
        programIdsInChannel.addProgramId("abc", false)
        Assert.assertEquals(2, programIdsInChannel.getBrowsableProgramCount())
    }

    @Test
    fun getBrowsableProgramIds() {
        // A new object should have no content
        Assert.assertTrue(programIdsInChannel.getBrowsableProgramIds().isEmpty())

        // Create a Set of browsable IDs
        val browsableIds = HashSet<String>().apply {
            add("123")
            add("456")
        }

        // Add them
        browsableIds.forEach {
            programIdsInChannel.addProgramId(it, true)
        }

        // The Set of browsable content should now match
        Assert.assertEquals(browsableIds, programIdsInChannel.getBrowsableProgramIds())

        // There shouldn't be any non-browsable content
        Assert.assertTrue(programIdsInChannel.getNonBrowsableProgramIds().isEmpty())
    }

    @Test
    fun getNonBrowsableProgramIds() {
        // A new object should have no content
        Assert.assertTrue(programIdsInChannel.getNonBrowsableProgramIds().isEmpty())

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
        Assert.assertEquals(nonBrowsableIds, programIdsInChannel.getNonBrowsableProgramIds())

        // There shouldn't be any browsable content
        Assert.assertTrue(programIdsInChannel.getBrowsableProgramIds().isEmpty())
    }
}
