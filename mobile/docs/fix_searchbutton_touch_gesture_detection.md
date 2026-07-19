### Fix Documentation

#### Problem

- Testing on android expo go with expo 54 and 55.
- The button or box is pressable when it is not currently sticky to the header which was achieved by setting stickyHeaderIndices prop on a parent ScrollView component.
- On sticky scroll behaviour, the component does not detect touch or press gestures.

#### Solution

- While seeming temporary workaround, simply changing the Pressable prop from onPress to onPressIn fixes the touch gesture.

#### Fix Source

- After months, I found the fix by keyword searching google from which I proceeded onto [react-native github](https://github.com/react/react-native/issues/51763) as an issue with multiple reproducible reports.
