import { Screen } from '@/components/core';
import { SocialConnections } from '@/components/reusables/social-connections';
import { Button } from '@/components/reusables/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/reusables/ui/card';
import { Input } from '@/components/reusables/ui/input';
import { Label } from '@/components/reusables/ui/label';
import { Separator } from '@/components/reusables/ui/separator';
import { Text } from '@/components/reusables/ui/text';
import Snackbar, { type SnackbarVariant } from '@/components/ui/snack-bar';
import { Colors } from '@/constants';
import { useRegister } from '@/features/auth/hook';
import { registerSchema, type RegisterSchema } from '@/features/auth/schema';
import useForm from '@/hooks/use-app-form';
import { useAppTheme } from '@/hooks/use-app-theme';
import { router } from 'expo-router';
import React, { useRef, useState } from 'react';
import { TextInput, View } from 'react-native';
import GoldSubmitButton from '../components/gold-submit-button';

const Register = () => {
  const { color } = useAppTheme();
  const [snackBar, setSnackBar] = useState<{ message: string; variant: SnackbarVariant } | null>(null);
  const { mutate: register, isPending } = useRegister();
  const [confirmPassword, setConfirmPassword] = React.useState('');
  const { form, errors, handleChange, handleSubmit } = useForm<RegisterSchema>({
    data: {
      firstName: '',
      lastName: '',
      email: '',
      password: '',
    },
    schema: registerSchema,
    onSubmit: (data) => {
      if (confirmPassword !== data.password) return;
      register(data);
    },
  });

  const lastNameRef = useRef<TextInput>(null);
  const emailRef = useRef<TextInput>(null);
  const passwordRef = useRef<TextInput>(null);
  const confirmPasswordRef = useRef<TextInput>(null);

  function handleSocialSignIn(type: string) {
    setSnackBar({ message: 'Coming soon!', variant: 'info' });
  }

  const passwordMismatchError =
    confirmPassword.length > 0 && form.password.length > 0 && confirmPassword !== form.password ? 'Passwords do not match' : null;

  return (
    <Screen
      keyboard
      scrollable
      keyboardDismissMode="interactive"
      keyboardShouldPersistTaps="handled"
      contentContainerClassName="sm:flex-1 items-center justify-center px-4 pb-8 sm:py-4 sm:p-6 mt-safe"
      secondaryBackground
      edges={[]}
    >
      <View className="w-full max-w-sm">
        <View className="gap-6">
          <Card className="bg-transparent border-0">
            <CardHeader>
              <CardTitle className="text-center text-xl sm:text-left" style={{ color: color.goldText }}>
                Create your account
              </CardTitle>
              <CardDescription className="text-center sm:text-left">
                Welcome! Please fill in the details to get started.
              </CardDescription>
            </CardHeader>
            <CardContent className="gap-6">
              <View className="gap-1.5">
                <View className="gap-1">
                  <Label htmlFor="firstName">First name</Label>
                  <Input
                    id="firstName"
                    autoComplete="given-name"
                    editable={!isPending}
                    value={form.firstName}
                    onChangeText={(text) => handleChange('firstName', text.trim())}
                    returnKeyType="next"
                    onSubmitEditing={() => lastNameRef.current?.focus()}
                  />
                  <Text className="text-sm" style={{ color: Colors.warning }}>
                    {errors.firstName}
                  </Text>
                </View>
                <View className="gap-1">
                  <Label htmlFor="lastName">Last name</Label>
                  <Input
                    id="lastName"
                    autoComplete="family-name"
                    editable={!isPending}
                    value={form.lastName}
                    onChangeText={(text) => handleChange('lastName', text.trim())}
                    returnKeyType="next"
                    ref={lastNameRef}
                    onSubmitEditing={() => emailRef.current?.focus()}
                  />
                  <Text className="text-sm" style={{ color: Colors.warning }}>
                    {errors.lastName}
                  </Text>
                </View>
                <View className="gap-1">
                  <Label htmlFor="email">Email</Label>
                  <Input
                    id="email"
                    placeholder="m@example.com"
                    keyboardType="email-address"
                    autoComplete="email"
                    autoCapitalize="none"
                    editable={!isPending}
                    value={form.email}
                    onChangeText={(text) => handleChange('email', text.trim())}
                    returnKeyType="next"
                    ref={emailRef}
                    onSubmitEditing={() => passwordRef.current?.focus()}
                  />
                  <Text className="text-sm" style={{ color: Colors.warning }}>
                    {errors.email}
                  </Text>
                </View>
                <View className="gap-1">
                  <View className="flex-row items-center">
                    <Label htmlFor="password">Password</Label>
                  </View>
                  <Input
                    id="password"
                    secureTextEntry
                    returnKeyType="send"
                    editable={!isPending}
                    value={form.password}
                    onChangeText={(text) => handleChange('password', text)}
                    ref={passwordRef}
                    onSubmitEditing={() => confirmPasswordRef.current?.focus()}
                  />
                  <Text className="text-sm" style={{ color: Colors.warning }}>
                    {errors.password}
                  </Text>
                </View>
                <View className="gap-1">
                  <View className="flex-row items-center">
                    <Label htmlFor="confirmPassword">Confirm Password</Label>
                  </View>
                  <Input
                    id="confirmPassword"
                    secureTextEntry
                    returnKeyType="send"
                    submitBehavior="submit"
                    editable={!isPending}
                    value={confirmPassword}
                    onChangeText={(text) => setConfirmPassword(text)}
                    ref={confirmPasswordRef}
                  />
                  <Text className="text-sm" style={{ color: Colors.warning }}>
                    {passwordMismatchError || ''}
                  </Text>
                </View>

                <GoldSubmitButton
                  onPress={handleSubmit}
                  isPending={isPending}
                  title="Continue"
                  disabled={isPending || confirmPassword !== form.password}
                />
              </View>

              <Button
                variant="outline"
                onPress={() => router.push('/login')}
                className="w-full font-semibold flex-row items-center justify-center gap-1"
                style={{ borderColor: color.goldBorder, backgroundColor: 'transparent' }}
              >
                <Text className="text-sm leading-4">
                  <Text style={{ color: color.goldText }}>Already have an account? Sign in</Text>
                </Text>
              </Button>
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

      <Snackbar
        message={snackBar?.message || ''}
        variant={snackBar?.variant || 'info'}
        visible={!!snackBar}
        onHide={() => setSnackBar(null)}
        style={{ top: 0 }}
      />
    </Screen>
  );
};
export default Register;
