import { Tabs } from 'expo-router';

import { Icon } from '@/components/core';
import { HapticTab } from '@/components/haptic-tab';
import { Colors, FontSize } from '@/constants';
import type { SearchStateType } from '@/screens/search/hooks/use-search-state';

export default function TabLayout() {
  return (
    <Tabs
      screenOptions={{
        tabBarActiveTintColor: Colors.goldIcon,
        headerShown: false,
        tabBarButton: HapticTab,
        tabBarStyle: {
          backgroundColor: Colors.tabBackground,
          minHeight: 75,
        },
        tabBarIconStyle: { marginTop: 5 },
        tabBarLabelStyle: {
          fontSize: FontSize.sm,
        },
      }}
    >
      <Tabs.Screen
        name="home"
        options={{
          title: 'Home',
          tabBarIcon: ({ focused, color }) => <Icon size={28} name={focused ? 'home' : 'home.outline'} color={color} />,
        }}
      />
      <Tabs.Screen
        name="search"
        options={{
          title: 'Search',
          tabBarIcon: ({ focused, color }) => <Icon size={28} name={focused ? 'search' : 'search.outline'} color={color} />,
        }}
        listeners={({ route, navigation }) => ({
          tabPress: (e) => {
            if (navigation.isFocused()) {
              e.preventDefault();

              navigation.navigate('search', {
                searchState:
                  (route.params as { searchState?: SearchStateType })?.searchState === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE',
              } satisfies { searchState: SearchStateType });
            } else {
              e.preventDefault();

              navigation.navigate('search');
            }
          },
        })}
      />
      <Tabs.Screen
        name="library"
        options={{
          title: 'Library',
          tabBarIcon: ({ focused, color }) => <Icon size={28} name={focused ? 'library' : 'library.outline'} color={color} />,
        }}
      />
      <Tabs.Screen
        name="activity"
        options={{
          title: 'Activity',
          tabBarIcon: ({ focused, color }) => <Icon size={28} name={focused ? 'activity' : 'activity.outline'} color={color} />,
        }}
      />
      <Tabs.Screen
        name="profile"
        options={{
          title: 'Profile',
          tabBarIcon: ({ focused, color }) => <Icon size={28} name={focused ? 'profile' : 'profile.outline'} color={color} />,
        }}
      />
    </Tabs>
  );
}
