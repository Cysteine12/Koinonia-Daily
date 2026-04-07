import { Screen } from '@/components/core';
import { SocialConnections } from '@/components/reusables/social-connections';
import { Button } from '@/components/reusables/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/reusables/ui/card';
import { Input } from '@/components/reusables/ui/input';
import { Label } from '@/components/reusables/ui/label';
import { Separator } from '@/components/reusables/ui/separator';
import { Text } from '@/components/reusables/ui/text';
import GoldGradient from '@/components/ui/gold-gradient';
import { useRegister } from '@/features/auth/hook';
import { registerSchema, type RegisterSchema } from '@/features/auth/schema';
import useForm from '@/hooks/use-app-form';
import { router } from 'expo-router';
import React, { useRef } from 'react';
import { ActivityIndicator, TextInput, View } from 'react-native';

const Register = () => {
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
    // TODO
  }

  return (
    <Screen
      keyboard
      scrollable
      keyboardDismissMode="interactive"
      keyboardShouldPersistTaps="handled"
      contentContainerClassName="sm:flex-1 items-center justify-center px-4 pb-8 sm:py-4 sm:p-6 mt-safe"
      edges={[]}
    >
      <View className="w-full max-w-sm">
        <View className="gap-6">
          <Card className="bg-transparent border-0">
            <CardHeader>
              <CardTitle className="text-gold-text text-center text-xl sm:text-left">Create your account</CardTitle>
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
                    onChangeText={(text) => handleChange('firstName', text)}
                    returnKeyType="next"
                    onSubmitEditing={() => lastNameRef.current?.focus()}
                  />
                  <Text className="text-red-500 text-sm">{errors.firstName}</Text>
                </View>
                <View className="gap-1">
                  <Label htmlFor="lastName">Last name</Label>
                  <Input
                    id="lastName"
                    autoComplete="family-name"
                    editable={!isPending}
                    value={form.lastName}
                    onChangeText={(text) => handleChange('lastName', text)}
                    returnKeyType="next"
                    ref={lastNameRef}
                    onSubmitEditing={() => emailRef.current?.focus()}
                  />
                  <Text className="text-red-500 text-sm">{errors.lastName}</Text>
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
                    onChangeText={(text) => handleChange('email', text)}
                    returnKeyType="next"
                    ref={emailRef}
                    onSubmitEditing={() => passwordRef.current?.focus()}
                  />
                  <Text className="text-red-500 text-sm">{errors.email}</Text>
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
                  <Text className="text-red-500 text-sm">{errors.password}</Text>
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
                  <Text className="text-red-500 text-sm">
                    {(confirmPassword.length > 0 &&
                      form.password.length > 0 &&
                      confirmPassword !== form.password &&
                      'Passwords do not match') ||
                      ''}
                  </Text>
                </View>
                <GoldGradient>
                  <Button
                    className="bg-transparent w-full font-semibold"
                    onPress={handleSubmit}
                    disabled={isPending || confirmPassword !== form.password}
                  >
                    {isPending ? <ActivityIndicator /> : <Text className="text-black">Continue</Text>}
                  </Button>
                </GoldGradient>
              </View>
              <Button
                variant="outline"
                onPress={() => router.push('/login')}
                className="border-gold bg-transparent w-full font-semibold flex-row items-center justify-center gap-1"
              >
                <Text className="text-sm leading-4">
                  <Text className="text-gold-text">Already have an account? Sign in</Text>
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
    </Screen>
  );
};
export default Register;
