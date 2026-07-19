import { Icon, Text, View } from '@/components/core';
import BottomSheet from '@/components/ui/bottom-sheet';
import OpacityPressable from '@/components/ui/opacity-pressable';
import Tag from '@/components/ui/tag';
import { Colors, FontSize } from '@/constants';
import { useAppTheme } from '@/hooks/use-app-theme';
import { useState } from 'react';
import { Platform, ScrollView, TouchableOpacity } from 'react-native';
import DateTimePicker, { type DateTimePickerEvent } from '@react-native-community/datetimepicker';

interface FilterButtonProps {
  tags: string[];
  selectedFilterTags: string[];
  setSelectedFilterTags: (tags: string[]) => void;
  selectedDateRange: [Date | null, Date | null];
  setSelectedDateRange: (dateRanges: [Date | null, Date | null]) => void;
  onSubmit: (filterTags: string[], dateRange: [Date | null, Date | null]) => void;
}

export default function FilterButton({
  tags,
  selectedFilterTags,
  setSelectedFilterTags,
  selectedDateRange,
  setSelectedDateRange,
  onSubmit,
}: FilterButtonProps) {
  const { color, isDark } = useAppTheme();
  const today = new Date();
  const [isVisible, setVisible] = useState(false);

  // Local draft state — only committed on submit
  const [draftTags, setDraftTags] = useState<string[]>(selectedFilterTags);
  const [draftFrom, setDraftFrom] = useState<Date | null>(selectedDateRange[0]);
  const [draftTo, setDraftTo] = useState<Date | null>(selectedDateRange[1]);

  // Android only: controls whether each picker dialog is open
  const [showFromPicker, setShowFromPicker] = useState(false);
  const [showToPicker, setShowToPicker] = useState(false);

  const isButtonActive = selectedFilterTags.length > 0 || selectedDateRange[0] || selectedDateRange[1];
  const isIOS = Platform.OS === 'ios';

  const openSheet = () => {
    setDraftTags(selectedFilterTags);
    setDraftFrom(selectedDateRange[0]);
    setDraftTo(selectedDateRange[1]);
    setShowFromPicker(false);
    setShowToPicker(false);
    setVisible(true);
  };

  const MAX_TAGS = 3;

  const handleTagClick = (tag: string) => {
    if (draftTags.includes(tag)) {
      setDraftTags(draftTags.filter((t) => t !== tag));
    } else if (draftTags.length < MAX_TAGS) {
      setDraftTags([...draftTags, tag]);
    }
  };

  const handleSubmit = () => {
    setSelectedFilterTags(draftTags);
    setSelectedDateRange([draftFrom, draftTo]);
    onSubmit?.(draftTags, [draftFrom, draftTo]);
    setVisible(false);
  };

  const handleFromChange = (event: DateTimePickerEvent, date?: Date) => {
    setShowFromPicker(false);
    if (event.type === 'set' && date) {
      setDraftFrom(date);
    }
  };

  const handleToChange = (event: DateTimePickerEvent, date?: Date) => {
    setShowToPicker(false);
    if (event.type === 'set' && date) {
      setDraftTo(date);
    }
  };

  const midpoint = Math.ceil(tags.length / 2);
  const topRow = tags.slice(0, midpoint);
  const bottomRow = tags.slice(midpoint);

  const renderTag = (tag: string) => (
    <OpacityPressable key={tag} activeScale={0.95} onPress={() => handleTagClick(tag)}>
      <Tag
        text={tag}
        borderColor={draftTags.includes(tag) ? color.goldBorder : color.border}
        color={draftTags.includes(tag) ? color.goldBorder : color.border}
        backgroundColor={draftTags.includes(tag) ? color.goldTextMuted : color.cardBorder}
        textColor={draftTags.includes(tag) ? color.goldText : color.text}
        fontSize={FontSize.xs + 2}
        className="mr-2 py-1 px-3 rounded-xl"
      />
    </OpacityPressable>
  );

  /** Formats a date as "Jan 12, 2025" */
  const formatDate = (d: Date) =>
    d.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });

  const applyButton = (
    <TouchableOpacity
      onPress={handleSubmit}
      className="rounded-xl items-center py-3.5"
      style={{ backgroundColor: color.goldBorder }}
    >
      <Text weight="bold" size={FontSize.sm + 2} style={{ color: isDark ? color.background : '#fff' }}>
        Apply Filters
      </Text>
    </TouchableOpacity>
  );

  return (
    <>
      <OpacityPressable
        onPress={openSheet}
        className="border py-1 px-2 rounded-lg flex-row items-center ml-1"
        style={{
          backgroundColor: color[isButtonActive ? 'inputBackground' : 'background'],
          borderColor: color[isButtonActive ? 'goldBorder' : 'border'],
        }}
      >
        <Icon name="filter" size={FontSize.sm} color={isButtonActive ? Colors.goldIcon : color.icon} />
        <Text weight="semibold" size={FontSize.sm + 2} className="ml-1" style={{ color: color[isButtonActive ? 'goldText' : 'text'] }}>
          Filters
        </Text>
      </OpacityPressable>

      <BottomSheet
        visible={isVisible}
        onClose={() => setVisible(false)}
        title="Filters"
        maxHeight={0.72}
        footer={applyButton}
      >
        {/* ── Date Range ── */}
        <Text
          weight="semibold"
          size={FontSize.sm + 1}
          className="mb-3"
          style={{ color: color.textMuted }}
        >
          Date Range
        </Text>

        <View className="flex-row gap-3 mb-6">
          {/* From */}
          <View className="flex-1">
            <Text
              weight="semibold"
              size={FontSize.sm}
              className="mb-2"
              style={{ color: color.text }}
            >
              From
            </Text>
            <View
              className="rounded-xl overflow-hidden"
              style={{
                borderWidth: 1,
                borderColor: draftFrom ? color.goldBorder : color.border,
                backgroundColor: color.inputBackground,
              }}
            >
              {isIOS ? (
                /* iOS: compact picker renders as a tappable button inline */
                <View className="flex-row items-center justify-between px-3 py-2">
                  <DateTimePicker
                    value={draftFrom ?? new Date()}
                    mode="date"
                    display="compact"
                    maximumDate={draftTo ?? today}
                    themeVariant={isDark ? 'dark' : 'light'}
                    onChange={handleFromChange}
                    style={{ marginLeft: -8 }}
                  />
                  {draftFrom && (
                    <TouchableOpacity onPress={() => setDraftFrom(null)} hitSlop={8}>
                      <Icon name="close.circle" size={FontSize.sm + 2} color={color.textMuted} />
                    </TouchableOpacity>
                  )}
                </View>
              ) : (
                /* Android: tappable row that opens dialog picker */
                <>
                  <TouchableOpacity
                    className="flex-row items-center justify-between px-3 py-3"
                    onPress={() => setShowFromPicker(true)}
                    activeOpacity={0.7}
                  >
                    <Text size={FontSize.sm} style={{ color: draftFrom ? color.text : color.textMuted }}>
                      {draftFrom ? formatDate(draftFrom) : 'Select date'}
                    </Text>
                    <View className="flex-row items-center gap-1">
                      {draftFrom ? (
                        <TouchableOpacity onPress={() => setDraftFrom(null)} hitSlop={8}>
                          <Icon name="close.circle" size={FontSize.sm + 2} color={color.textMuted} />
                        </TouchableOpacity>
                      ) : (
                        <Icon name="calendar.outline" size={FontSize.sm + 2} color={color.textMuted} />
                      )}
                    </View>
                  </TouchableOpacity>
                  {showFromPicker && (
                    <DateTimePicker
                      value={draftFrom ?? new Date()}
                      mode="date"
                      display="default"
                      maximumDate={draftTo ?? today}
                      themeVariant={isDark ? 'dark' : 'light'}
                      onChange={handleFromChange}
                    />
                  )}
                </>
              )}
            </View>
          </View>

          {/* To */}
          <View className="flex-1">
            <Text
              weight="semibold"
              size={FontSize.sm}
              className="mb-2"
              style={{ color: color.text }}
            >
              To
            </Text>
            <View
              className="rounded-xl overflow-hidden"
              style={{
                borderWidth: 1,
                borderColor: draftTo ? color.goldBorder : color.border,
                backgroundColor: color.inputBackground,
              }}
            >
              {isIOS ? (
                /* iOS: compact picker renders as a tappable button inline */
                <View className="flex-row items-center justify-between px-3 py-2">
                  <DateTimePicker
                    value={draftTo ?? new Date()}
                    mode="date"
                    display="compact"
                    minimumDate={draftFrom ?? undefined}
                    maximumDate={today}
                    themeVariant={isDark ? 'dark' : 'light'}
                    onChange={handleToChange}
                    style={{ marginLeft: -8 }}
                  />
                  {draftTo && (
                    <TouchableOpacity onPress={() => setDraftTo(null)} hitSlop={8}>
                      <Icon name="close.circle" size={FontSize.sm + 2} color={color.textMuted} />
                    </TouchableOpacity>
                  )}
                </View>
              ) : (
                /* Android: tappable row that opens dialog picker */
                <>
                  <TouchableOpacity
                    className="flex-row items-center justify-between px-3 py-3"
                    onPress={() => setShowToPicker(true)}
                    activeOpacity={0.7}
                  >
                    <Text size={FontSize.sm} style={{ color: draftTo ? color.text : color.textMuted }}>
                      {draftTo ? formatDate(draftTo) : 'Select date'}
                    </Text>
                    <View className="flex-row items-center gap-1">
                      {draftTo ? (
                        <TouchableOpacity onPress={() => setDraftTo(null)} hitSlop={8}>
                          <Icon name="close.circle" size={FontSize.sm + 2} color={color.textMuted} />
                        </TouchableOpacity>
                      ) : (
                        <Icon name="calendar.outline" size={FontSize.sm + 2} color={color.textMuted} />
                      )}
                    </View>
                  </TouchableOpacity>
                  {showToPicker && (
                    <DateTimePicker
                      value={draftTo ?? new Date()}
                      mode="date"
                      display="default"
                      minimumDate={draftFrom ?? undefined}
                      maximumDate={today}
                      themeVariant={isDark ? 'dark' : 'light'}
                      onChange={handleToChange}
                    />
                  )}
                </>
              )}
            </View>
          </View>
        </View>

        {/* ── Tags ── */}
        <View className="flex-row items-center justify-between mb-3">
          <Text weight="semibold" size={FontSize.sm + 1} style={{ color: color.textMuted }}>
            Tags
          </Text>
          <Text size={FontSize.xs + 1} style={{ color: draftTags.length >= MAX_TAGS ? color.goldText : color.textMuted }}>
            {draftTags.length} / {MAX_TAGS}
          </Text>
        </View>
        <ScrollView horizontal showsHorizontalScrollIndicator={false} keyboardShouldPersistTaps="always" className="mb-2">
          <View>
            <View className="flex-row mb-2">{topRow.map(renderTag)}</View>
            <View className="flex-row">{bottomRow.map(renderTag)}</View>
          </View>
        </ScrollView>
        <Text size={FontSize.xs} className="mt-2 mb-6" style={{ color: color.textMuted }}>
          Select up to {MAX_TAGS} tags to filter by
        </Text>
      </BottomSheet>
    </>
  );
}
