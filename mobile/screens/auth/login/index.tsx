import { Screen, View } from '@/components/core';
import { SocialConnections } from '@/components/reusables/social-connections';
import { Button } from '@/components/reusables/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/reusables/ui/card';
import { Input } from '@/components/reusables/ui/input';
import { Label } from '@/components/reusables/ui/label';
import { Separator } from '@/components/reusables/ui/separator';
import { Text } from '@/components/reusables/ui/text';
import { Colors } from '@/constants';
import { useLogin } from '@/features/auth/hook';
import { type LoginSchema, loginSchema } from '@/features/auth/schema';
import useForm from '@/hooks/use-app-form';
import { useAppTheme } from '@/hooks/use-app-theme';
import { router } from 'expo-router';
import { useRef } from 'react';
import type { TextInput } from 'react-native';
import { Image } from 'react-native';
import GoldSubmitButton from '../components/gold-submit-button';

const Login = () => {
  const { color } = useAppTheme();
  const { mutate: login, isPending } = useLogin();
  const { form, errors, handleChange, handleSubmit } = useForm<LoginSchema>({
    data: {
      email: '',
      password: '',
    },
    schema: loginSchema,
    onSubmit: (data) => login(data),
  });

  const passwordRef = useRef<TextInput>(null);

  const handleSocialSignIn = (type: string) => {
    // TODO
  };

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
        <Image source={require('@/assets/images/logo.png')} className="size-24 rounded-full self-center" />
        <View className="gap-6">
          <Card className="bg-transparent border-0">
            <CardHeader>
              <CardTitle className="text-center text-xl sm:text-left" style={{ color: color.goldText }}>
                Sign in to your app
              </CardTitle>
              <CardDescription className="text-center sm:text-left">Welcome back! Please sign in to continue</CardDescription>
            </CardHeader>
            <CardContent className="gap-6">
              <View className="gap-1.5">
                <View className="gap-1">
                  <Label htmlFor="email">Email</Label>
                  <Input
                    id="email"
                    placeholder="m@example.com"
                    keyboardType="email-address"
                    autoComplete="email"
                    autoCapitalize="none"
                    returnKeyType="next"
                    submitBehavior="submit"
                    editable={!isPending}
                    value={form.email}
                    onChangeText={(text) => handleChange('email', text.trim())}
                    onSubmitEditing={() => passwordRef.current?.focus()}
                  />
                  <Text className="text-sm" style={{ color: Colors.warning }}>
                    {errors?.email}
                  </Text>
                </View>
                <View className="gap-1">
                  <View className="flex-row items-center">
                    <Label htmlFor="password">Password</Label>
                    <Button
                      variant="ghost"
                      size="sm"
                      className="ml-auto h-4 px-1 py-0 sm:h-4 no-underline"
                      onPress={() => router.push({ pathname: '/forgot-password', params: { email: form.email } })}
                    >
                      <Text className="font-normal leading-4" style={{ color: color.goldText }}>
                        Forgot your password?
                      </Text>
                    </Button>
                  </View>
                  <Input
                    id="password"
                    secureTextEntry
                    returnKeyType="send"
                    editable={!isPending}
                    value={form.password}
                    onChangeText={(text) => handleChange('password', text)}
                    ref={passwordRef}
                    onSubmitEditing={handleSubmit}
                  />
                  <Text className="text-sm" style={{ color: Colors.warning }}>
                    {errors?.password}
                  </Text>
                </View>

                <GoldSubmitButton onPress={handleSubmit} isPending={isPending} title="Continue" />
              </View>

              <Button
                variant="outline"
                onPress={() => router.push('/register')}
                className="w-full font-semibold flex-row items-center justify-center gap-1"
                style={{ borderColor: color.goldBorder, backgroundColor: 'transparent' }}
              >
                <Text className="text-sm leading-4">
                  <Text style={{ color: color.goldText }}>Don&apos;t have an account? Sign up</Text>
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

export default Login;
