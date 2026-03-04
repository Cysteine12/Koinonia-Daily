import { SocialConnections } from '@/components/reusables/social-connections';
import { Button } from '@/components/reusables/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/reusables/ui/card';
import { Input } from '@/components/reusables/ui/input';
import { Label } from '@/components/reusables/ui/label';
import { Separator } from '@/components/reusables/ui/separator';
import { Text } from '@/components/reusables/ui/text';
import GoldGradient from '@/components/ui/gold-gradient';
import { useLogin } from '@/features/auth/hook';
import { type LoginSchema, loginSchema } from '@/features/auth/schema';
import useForm from '@/hooks/use-app-form';
import { router } from 'expo-router';
import { useRef } from 'react';
import type { TextInput } from 'react-native';
import { ActivityIndicator, Image, KeyboardAvoidingView, Platform, ScrollView, View } from 'react-native';

const Login = () => {
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
    <KeyboardAvoidingView
      className="flex-1"
      behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
      keyboardVerticalOffset={Platform.OS === 'ios' ? 80 : 0}
    >
      <ScrollView
        keyboardShouldPersistTaps="handled"
        contentContainerClassName="sm:flex-1 items-center justify-center p-4 py-8 sm:py-4 sm:p-6 mt-safe"
        keyboardDismissMode="interactive"
      >
        <View className="w-full max-w-sm">
          <Image source={require('@/assets/images/logo.jpg')} className="size-24 rounded-full self-center" />
          <View className="gap-6">
            <Card className="bg-transparent border-0">
              <CardHeader>
                <CardTitle className="text-center text-gold-text text-xl sm:text-left">Sign in to your app</CardTitle>
                <CardDescription className="text-center sm:text-left">
                  Welcome back! Please sign in to continue
                </CardDescription>
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
                      onChangeText={(text) => handleChange('email', text)}
                      onSubmitEditing={() => passwordRef.current?.focus()}
                    />
                    <Text className="text-sm text-destructive">{errors?.email}</Text>
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
                        <Text className="text-gold-text font-normal leading-4">Forgot your password?</Text>
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
                    <Text className="text-sm text-destructive">{errors?.password}</Text>
                  </View>
                  <GoldGradient>
                    <Button className="bg-transparent w-full font-semibold" onPress={handleSubmit} disabled={isPending}>
                      {isPending ? <ActivityIndicator /> : <Text className="text-black">Continue</Text>}
                    </Button>
                  </GoldGradient>
                </View>
                <Button
                  variant="outline"
                  onPress={() => router.push('/register')}
                  className="border-gold bg-transparent w-full font-semibold flex-row items-center justify-center gap-1"
                >
                  <Text className="text-sm leading-4">
                    <Text>Don&apos;t have an account? </Text>
                    <Text className="text-gold-text">Sign up</Text>
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
      </ScrollView>
    </KeyboardAvoidingView>
  );
};

export default Login;
