# Leanback UI Toolkit to Compose Migration Sample

This sample demonstrates how to migrate an Android TV application from the deprecated Leanback UI Toolkit to **Jetpack Compose for TV**. It serves as a reference implementation showing before-and-after states for common TV UI components.

## Getting Started

- Open the `LeanbackToCompose` project in Android Studio.
- Compile and deploy to your Android TV emulator or device (API 23+).

## Explore the Migration

The sample demonstrates migration of the following key features:
- **Onboarding Flow**: Guided introduction to the app.
- **Catalog Browsing**: Catalog of videos grouped by category using TV-optimized lazy rows/columns.
- **Immersive Details**: Rich details view with related content recommendations.
- **Media Playback**: Integrated media player using Media3 ExoPlayer.
- **Search**: In-app search functionality.
- **Settings & Preferences**: TV-optimized settings panel.
- **Guided Steps**: Multi-step user flows (e.g., subscription wizard).

---

## Migration Showcase (Before vs. After)

Here is a side-by-side comparison of the legacy Leanback UI (left) and the modernized Jetpack Compose for TV UI (right).

| Component / Screen | Leanback (Before) | Compose (After) |
| :--- | :---: | :---: |
| **Onboarding**<br>Introduces users to the app features. | ![Onboarding Before](screenshots/before/onboarding.png) | ![Onboarding After](screenshots/after/onboarding.png) |
| **Browse (Home & Card Views)**<br>Main landing screen with categorized content and horizontal card lists. | ![Browse Before](screenshots/before/browse.png)<br><br>![Row Before](screenshots/before/row.png) | ![Browse After](screenshots/after/browse.png) |
| **Details Screen**<br>In-depth information about a specific video. | ![Details Before](screenshots/before/details.png) | ![Details After](screenshots/after/details.png) |
| **Details Row**<br>Related content rows within the details screen. | ![Details Row Before](screenshots/before/details_row.png) | ![Details Row After](screenshots/after/details_row.png) |
| **Vertical Grid**<br>Freeform vertical grid of content. | ![Vertical Grid Before](screenshots/before/vertical_grid.png) | ![Vertical Grid After](screenshots/after/vertical_grid.png) |
| **Search**<br>In-app search interface. | ![Search Before](screenshots/before/search.png) | ![Search After](screenshots/after/search.png) |
| **Guided Step (Wizard)**<br>Multi-step decision flow (e.g., subscription). | ![Guided Step Before](screenshots/before/guided_step.png) | ![Guided Step After](screenshots/after/guided_step.png) |
| **Settings**<br>Side-panel settings overlay. | ![Settings Before](screenshots/before/settings.png) | ![Settings After](screenshots/after/settings.png) |
| **Error Screen**<br>Standardized error feedback. | ![Error Before](screenshots/before/error.png) | ![Error After](screenshots/after/error.png) |

### Generating Screenshots

To automatically navigate through the sample application and regenerate all 10 screenshot assets from a connected Android TV emulator or device, execute the included script:

```sh
./screenshots/capture_screenshots.sh [-s <serial>]
```

## Support

- Stack Overflow: [http://stackoverflow.com/questions/tagged/android-tv](http://stackoverflow.com/questions/tagged/android-tv)

## Key Dependencies

- **Jetpack Compose for TV** (`androidx.tv:tv-material`)
- **Media3 ExoPlayer** (`androidx.media3:media3-exoplayer`)
- **Coil** for image loading (`io.coil-kt:coil-compose`)

## Contributing

We love contributions! Please follow the steps in the [CONTRIBUTING guide](../CONTRIBUTING.md) to get started.

## License

See the [LICENSE](../LICENSE) file for details.
