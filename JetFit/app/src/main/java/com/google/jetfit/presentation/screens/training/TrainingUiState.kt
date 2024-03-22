package com.google.jetfit.presentation.screens.training

import com.google.jetfit.R


data class TrainingUiState(
    val isFilterExpended: Boolean = false,
    val isSortExpended: Boolean = false,
    val filterSideMenuUiState: FilterSideMenuUiState = FilterSideMenuUiState(),
    val instructorList: List<String> = emptyList(),
    val selectedSortItem: Int = 0,
){
    enum class SortItem {
        Newest,Relevance, Oldest
    }

    val filterItems = listOf(
        TrainingFilterData(
            icon = R.drawable.length_ic,
            title = R.string.length,
            description = filterSideMenuUiState.videoLength.value
        ),
        TrainingFilterData(
            icon = R.drawable.person_ic,
            title = R.string.instructor,
            description = filterSideMenuUiState.instructor
        ),
        TrainingFilterData(
            icon = R.drawable.class_type_ic,
            title = R.string.class_type,
            description = filterSideMenuUiState.classType.name
        ),
        TrainingFilterData(
            icon = R.drawable.language_ic,
            title = R.string.class_language,
            description = filterSideMenuUiState.classLanguage.name
        ),
        TrainingFilterData(
            icon = R.drawable.difficulty_ic,
            title = R.string.difficulty,
            description = filterSideMenuUiState.difficulty.name
        ),
        TrainingFilterData(
            icon = R.drawable.subtitle_ic,
            title = R.string.subtitles,
            description = filterSideMenuUiState.subtitles.name
        )
    )

}
data class TrainingFilterData(
    val icon: Int,
    val title: Int,
    val description: String,
)
data class FilterSideMenuUiState(
    val videoLength: VideoLength = VideoLength.SHORT,
    val instructor: String = "",
    val classType:ClassType = ClassType.Strength,
    val difficulty: Difficulty = Difficulty.Beginner,
    val classLanguage:ClassLanguage= ClassLanguage.English,
    val subtitles:Subtitles = Subtitles.Available,
){
    enum class VideoLength(val value: String) {
        SHORT("15-30"), MEDIUM("30-45"), LONG("45-60")
    }
    enum class ClassType {
        Strength
    }

    enum class Difficulty {
        Beginner, Intermediate, Advanced
    }
    enum class ClassLanguage {
        English, Spanish, French, German, Italian, Russian, Japanese, Chinese, Korean, Arabic
    }

    enum class Subtitles {
        Available , NotAvailable
    }

}

