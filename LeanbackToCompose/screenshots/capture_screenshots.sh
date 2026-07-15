#!/bin/bash
#
# Helper script to automatically navigate through the Leanback sample application
# and generate screenshots for all UI components and fragments.
#
# Usage:
#   ./capture_screenshots.sh [-s <serial>]
#   ANDROID_SERIAL=localhost:42001 ./capture_screenshots.sh
#

set -e

SERIAL=${ANDROID_SERIAL:-}
while [[ $# -gt 0 ]]; do
  case $1 in
    -s|--serial)
      SERIAL="$2"
      shift 2
      ;;
    *)
      echo "Unknown option: $1"
      echo "Usage: $0 [-s <serial>]"
      exit 1
      ;;
  esac
done

ADB=${ADB:-adb}
ADB_CMD="$ADB ${SERIAL:+-s $SERIAL}"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
OUTPUT_DIR="$SCRIPT_DIR/after"
mkdir -p "$OUTPUT_DIR"

echo "=== Leanback Sample App Screenshot Generator ==="
echo "Output Directory: $OUTPUT_DIR"
if [[ -n "$SERIAL" ]]; then
  echo "Target Device: $SERIAL"
fi

echo "Checking ADB connection..."
$ADB_CMD devices | grep -E '\bdevice\b' > /dev/null || {
  echo "Error: No connected Android device or emulator found."
  echo "Please connect an Android TV emulator and try again."
  exit 1
}

echo "Returning to Home and clearing any system popups..."
$ADB_CMD shell input keyevent KEYCODE_HOME
sleep 1
$ADB_CMD shell input keyevent KEYCODE_BACK
sleep 1
$ADB_CMD shell input keyevent KEYCODE_BACK
sleep 1

capture_screen() {
  local filename=$1
  local title=$2
  echo "Capturing: $title -> screenshots/$filename"
  $ADB_CMD exec-out screencap -p > "$OUTPUT_DIR/$filename"
}

echo "=== 1/10: Onboarding Fragment ==="
$ADB_CMD shell am start -S -n com.example.android.tvleanback/.ui.OnboardingActivity
sleep 11
capture_screen "onboarding.png" "Onboarding Fragment"

echo "=== Completing Onboarding sequence ==="
for i in 1 2 3 4; do
  $ADB_CMD shell input keyevent KEYCODE_DPAD_RIGHT
  sleep 1
  $ADB_CMD shell input keyevent KEYCODE_DPAD_CENTER
  sleep 1
done
sleep 2

echo "=== 2/10: Browse Fragment ==="
$ADB_CMD shell am start -S -n com.example.android.tvleanback/.ui.MainActivity
sleep 11
capture_screen "browse.png" "Browse Fragment"

echo "=== 3/10: Row Fragment (Card Views) ==="
sleep 5
capture_screen "row.png" "Row Fragment (Card Views)"

echo "=== 4/10: Details Fragment ==="
$ADB_CMD shell input keyevent KEYCODE_DPAD_CENTER
sleep 7
capture_screen "details.png" "Details Fragment"

echo "=== 5/10: Row Fragment in Details Fragment ==="
$ADB_CMD shell input keyevent KEYCODE_DPAD_DOWN
sleep 1
$ADB_CMD shell input keyevent KEYCODE_DPAD_DOWN
sleep 5
capture_screen "details_row.png" "Row Fragment in Details Fragment"

echo "=== 6/10: Vertical Grid Fragment ==="
$ADB_CMD shell am start -S -n com.example.android.tvleanback/.ui.VerticalGridActivity
sleep 11
capture_screen "vertical_grid.png" "Vertical Grid Fragment"

echo "=== 7/10: Search Fragment ==="
$ADB_CMD shell am start -S -n com.example.android.tvleanback/.ui.SearchActivity
sleep 11
capture_screen "search.png" "Search Fragment"

echo "=== 8/10: Guided Step Fragment ==="
$ADB_CMD shell am start -S -n com.example.android.tvleanback/.ui.GuidedStepActivity
sleep 11
capture_screen "guided_step.png" "Guided Step Fragment"

echo "=== 9/10: Settings Fragment ==="
# Launch MainActivity first so BrowseFragment renders in the background under the side-panel
$ADB_CMD shell am start -S -n com.example.android.tvleanback/.ui.MainActivity
sleep 9
$ADB_CMD shell am start -n com.example.android.tvleanback/.ui.SettingsActivity
sleep 6
capture_screen "settings.png" "Settings Fragment"

echo "=== 10/10: Error Fragment ==="
$ADB_CMD shell am start -S -n com.example.android.tvleanback/.ui.MainActivity
sleep 7
$ADB_CMD shell input keyevent KEYCODE_DPAD_LEFT
sleep 1
for i in $(seq 1 6); do
  $ADB_CMD shell input keyevent KEYCODE_DPAD_DOWN
  sleep 1
done
$ADB_CMD shell input keyevent KEYCODE_DPAD_RIGHT
sleep 1
$ADB_CMD shell input keyevent KEYCODE_DPAD_RIGHT
sleep 1
$ADB_CMD shell input keyevent KEYCODE_DPAD_RIGHT
sleep 1
$ADB_CMD shell input keyevent KEYCODE_DPAD_CENTER
sleep 6
capture_screen "error.png" "Error Fragment"

echo ""
echo "=== ✅ All 10 screenshots successfully generated in: $OUTPUT_DIR ==="
