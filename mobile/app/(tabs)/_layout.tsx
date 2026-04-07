import { router, Tabs } from 'expo-router';
import React, { useEffect } from 'react';

import { Icon } from '@/components/core';
import { HapticTab } from '@/components/haptic-tab';
import { Colors, FontSize } from '@/constants';
import { useAuth } from '@/features/auth/auth-context';

export default function TabLayout() {
  const { isAuthenticated } = useAuth();

  useEffect(() => {
    if (!isAuthenticated) router.replace('/login');
  }, [isAuthenticated]);

  if (!isAuthenticated) {
    return null;
  }

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
          tabBarIcon: ({ color }) => <Icon size={28} name="home" color={color} />,
        }}
      />
      <Tabs.Screen
        name="search"
        options={{
          title: 'Search',
          tabBarIcon: ({ color }) => <Icon size={28} name="search" color={color} />,
        }}
      />
      <Tabs.Screen
        name="library"
        options={{
          title: 'Library',
          tabBarIcon: ({ color }) => <Icon size={28} name="library" color={color} />,
        }}
      />
      <Tabs.Screen
        name="activity"
        options={{
          title: 'Activity',
          tabBarIcon: ({ color }) => <Icon size={28} name="activity" color={color} />,
        }}
      />
      <Tabs.Screen
        name="profile"
        options={{
          title: 'Profile',
          tabBarIcon: ({ color }) => <Icon size={28} name="profile" color={color} />,
        }}
      />
    </Tabs>
  );
}
