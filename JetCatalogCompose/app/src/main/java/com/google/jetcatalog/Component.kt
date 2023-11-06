package com.google.jetcatalog

val components = listOf(
    Component.Button,
    Component.Card,
    Component.Chip,
    Component.ListItem,
    Component.ImmersiveList,
    Component.FeaturedCarousel,
    Component.NavigationDrawer,
    Component.TabRow,
    Component.ModalDialog,
    Component.TextField,
    Component.MediaPlayerOverlay,
)

val foundations = listOf(
    Foundation.Color,
    Foundation.Typography,
    Foundation.Motion,
    Foundation.Interaction,
)

enum class Foundation(val title: String, val imageArg: String, val routeValue: String) {
    Color(title = "Color", imageArg = "colors", routeValue = NavGraph.Color.routeName),
    Typography(title = "Typography", imageArg = "typography", routeValue = NavGraph.Typography.routeName),
    Motion(title = "Motion", imageArg = "motion", routeValue = NavGraph.Motion.routeName),
    Interaction(title = "Interaction", imageArg = "interaction", routeValue = NavGraph.Interaction.routeName),
}

enum class Component(val title: String, val imageArg: String, val routeValue: String) {
    Button(title = "Buttons", imageArg = "buttons", routeValue = NavGraph.Buttons.routeName),
    Card(title = "Cards", imageArg = "cards", routeValue = NavGraph.Cards.routeName),
    Chip(title = "Chips", imageArg = "chips", routeValue = NavGraph.Chips.routeName),
    ListItem(title = "Lists", imageArg = "lists", routeValue = NavGraph.Lists.routeName),
    ImmersiveList(
        title = "Immersive Cluster",
        imageArg = "immersive",
        routeValue = NavGraph.ImmersiveCluster.routeName
    ),
    FeaturedCarousel(
        title = "Featured Carousel",
        imageArg = "fc",
        routeValue = NavGraph.FeaturedCarousel.routeName
    ),
    NavigationDrawer(
        title = "Navigation Drawer",
        imageArg = "side_nav",
        routeValue = NavGraph.NavigationDrawer.routeName
    ),
    TabRow(title = "Tab Row", imageArg = "tabs", routeValue = NavGraph.TabRow.routeName),
    ModalDialog(
        title = "Modal Dialog",
        imageArg = "drawer",
        routeValue = NavGraph.ModalDialog.routeName
    ),
    TextField(title = "Text Field", imageArg = "input", routeValue = NavGraph.TextFields.routeName),
    MediaPlayerOverlay(
        title = "Video Player",
        imageArg = "player",
        routeValue = NavGraph.VideoPlayer.routeName
    )
}
