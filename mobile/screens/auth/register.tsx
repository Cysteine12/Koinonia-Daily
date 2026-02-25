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
import React from 'react';
import {
  ActivityIndicator,
  KeyboardAvoidingView,
  Platform,
  Pressable,
  ScrollView,
  useColorScheme,
  View,
} from 'react-native';

const Register = () => {
  const colorScheme = useColorScheme();
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
    onSubmit: (data) => register(data),
  });

  function handleSocialSignIn(type: string) {
    // TODO
  }

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
          <View className="gap-6">
            <Card className="bg-transparent border-0">
              <CardHeader>
                <CardTitle className="text-gold-text text-center text-xl sm:text-left">Create your account</CardTitle>
                <CardDescription className="text-center sm:text-left">
                  Welcome! Please fill in the details to get started.
                </CardDescription>
              </CardHeader>
              <CardContent className="gap-6">
                <View className="gap-6">
                  <View className="gap-1.5">
                    <Label htmlFor="firstName">First name</Label>
                    <Input
                      id="firstName"
                      autoComplete="given-name"
                      value={form.firstName}
                      onChangeText={(text) => handleChange('firstName', text)}
                      returnKeyType="next"
                      submitBehavior="submit"
                    />
                    {errors.firstName && <Text className="text-red-500 text-sm">{errors.firstName}</Text>}
                  </View>
                  <View className="gap-1.5">
                    <Label htmlFor="lastName">Last name</Label>
                    <Input
                      id="lastName"
                      autoComplete="family-name"
                      value={form.lastName}
                      onChangeText={(text) => handleChange('lastName', text)}
                      returnKeyType="next"
                      submitBehavior="submit"
                    />
                    {errors.lastName && <Text className="text-red-500 text-sm">{errors.lastName}</Text>}
                  </View>
                  <View className="gap-1.5">
                    <Label htmlFor="email">Email</Label>
                    <Input
                      id="email"
                      placeholder="m@example.com"
                      keyboardType="email-address"
                      autoComplete="email"
                      autoCapitalize="none"
                      value={form.email}
                      onChangeText={(text) => handleChange('email', text)}
                      returnKeyType="next"
                      submitBehavior="submit"
                    />
                    {errors.email && <Text className="text-red-500 text-sm">{errors.email}</Text>}
                  </View>
                  <View className="gap-1.5">
                    <View className="flex-row items-center">
                      <Label htmlFor="password">Password</Label>
                    </View>
                    <Input
                      id="password"
                      secureTextEntry
                      returnKeyType="send"
                      value={form.password}
                      onChangeText={(text) => handleChange('password', text)}
                    />
                    {errors.password && <Text className="text-red-500 text-sm">{errors.password}</Text>}
                  </View>
                  <View className="gap-1.5">
                    <View className="flex-row items-center">
                      <Label htmlFor="confirmPassword">Confirm Password</Label>
                    </View>
                    <Input
                      id="confirmPassword"
                      secureTextEntry
                      returnKeyType="send"
                      value={confirmPassword}
                      onChangeText={(text) => setConfirmPassword(text)}
                    />
                    {confirmPassword.length >= 8 && form.password.length >= 8 && confirmPassword !== form.password && (
                      <Text className="text-red-500 text-sm">Passwords do not match</Text>
                    )}
                  </View>
                  <GoldGradient>
                    <Button className="bg-transparent w-full" onPress={handleSubmit} disabled={isPending}>
                      {isPending ? (
                        <ActivityIndicator color={colorScheme === 'light' ? '#ffffff' : '#000000'} />
                      ) : (
                        <Text>Continue</Text>
                      )}
                    </Button>
                  </GoldGradient>
                </View>
                <Text className="text-center text-sm">
                  Already have an account?{' '}
                  <Pressable onPress={() => router.push('/login')}>
                    <Text className="text-sm text-gold-text leading-4">Sign in</Text>
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
    </KeyboardAvoidingView>
  );
};
export default Register;
