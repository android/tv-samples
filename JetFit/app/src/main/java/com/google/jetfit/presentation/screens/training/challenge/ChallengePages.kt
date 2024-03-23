package com.google.jetfit.presentation.screens.training.challenge

sealed class ChallengePages {
    data object ChallengeDetails : ChallengePages()
    data object ChallengeTabs : ChallengePages()
}