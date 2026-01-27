import { SocialConnections } from '@/components/reusables/social-connections';
import { Button } from '@/components/reusables/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/reusables/ui/card';
import { Input } from '@/components/reusables/ui/input';
import { Label } from '@/components/reusables/ui/label';
import { Separator } from '@/components/reusables/ui/separator';
import { Text } from '@/components/reusables/ui/text';
import { router } from 'expo-router';
import React from 'react';
import { Pressable, ScrollView, TextInput, View } from 'react-native';

const Register = () => {
  const passwordInputRef = React.useRef<TextInput>(null);

  function onEmailSubmitEditing() {
    passwordInputRef.current?.focus();
  }

  function onSubmit() {
    // TODO: Submit form and navigate to protected screen if successful
  }

  function handleSocialSignIn(type: string) {
    // TODO
  }

  return (
    <ScrollView
      keyboardShouldPersistTaps="handled"
      contentContainerClassName="sm:flex-1 items-center justify-center p-4 py-8 sm:py-4 sm:p-6 mt-safe"
      keyboardDismissMode="interactive"
    >
      <View className="w-full max-w-sm">
        <View className="gap-6">
          <Card className="border-border/0 sm:border-border shadow-none sm:shadow-sm sm:shadow-black/5">
            <CardHeader>
              <CardTitle className="text-center text-primary text-xl sm:text-left">Create your account</CardTitle>
              <CardDescription className="text-center sm:text-left">
                Welcome! Please fill in the details to get started.
              </CardDescription>
            </CardHeader>
            <CardContent className="gap-6">
              <View className="gap-6">
                <View className="gap-1.5">
                  <Label htmlFor="email">First name</Label>
                  <Input id="firstName" onSubmitEditing={() => {}} returnKeyType="next" submitBehavior="submit" />
                </View>
                <View className="gap-1.5">
                  <Label htmlFor="lastName">Last name</Label>
                  <Input id="lastName" onSubmitEditing={() => {}} returnKeyType="next" submitBehavior="submit" />
                </View>
                <View className="gap-1.5">
                  <Label htmlFor="email">Email</Label>
                  <Input
                    id="email"
                    placeholder="m@example.com"
                    keyboardType="email-address"
                    autoComplete="email"
                    autoCapitalize="none"
                    onSubmitEditing={onEmailSubmitEditing}
                    returnKeyType="next"
                    submitBehavior="submit"
                  />
                </View>
                <View className="gap-1.5">
                  <View className="flex-row items-center">
                    <Label htmlFor="password">Password</Label>
                  </View>
                  <Input
                    ref={passwordInputRef}
                    id="password"
                    secureTextEntry
                    returnKeyType="send"
                    onSubmitEditing={onSubmit}
                  />
                </View>
                <Button className="w-full" onPress={onSubmit}>
                  <Text>Continue</Text>
                </Button>
              </View>
              <Text className="text-center text-sm">
                Already have an account?{' '}
                <Pressable onPress={() => router.push('/login')}>
                  <Text className="text-sm underline underline-offset-4">Sign in</Text>
                </Pressable>
              </Text>
              <View className="flex-row items-center">
                <Separator className="flex-1" />
                <Text className="text-muted-foreground px-4 text-sm">or</Text>
                <Separator className="flex-1" />
              </View>
              <SocialConnections handleSocialSignIn={handleSocialSignIn} />
            </CardContent>
          </Card>
        </View>
      </View>
    </ScrollView>
  );
};
export default Register;
