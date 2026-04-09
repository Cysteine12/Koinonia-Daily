import { Screen } from '@/components/core';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/reusables/ui/card';
import { Input } from '@/components/reusables/ui/input';
import { Label } from '@/components/reusables/ui/label';
import BackButton from '@/components/ui/back-button';
import { useForgotPassword } from '@/features/auth/hook';
import { forgotPasswordSchema, type ForgotPasswordSchema } from '@/features/auth/schema';
import useForm from '@/hooks/use-app-form';
import { useAppTheme } from '@/hooks/use-app-theme';
import { useLocalSearchParams } from 'expo-router';
import React from 'react';
import { Text, View } from 'react-native';
import GoldSubmitButton from '../components/gold-submit-button';

const ForgotPassword = () => {
  const { color } = useAppTheme();
  const { email } = useLocalSearchParams<{ email?: string }>();
  const { mutate: forgotPassword, isPending } = useForgotPassword();
  const { form, errors, handleChange, handleSubmit } = useForm<ForgotPasswordSchema>({
    data: {
      email: email ?? '',
    },
    schema: forgotPasswordSchema,
    onSubmit: (data) => forgotPassword(data),
  });

  return (
    <Screen
      keyboard
      scrollable
      keyboardDismissMode="interactive"
      keyboardShouldPersistTaps="handled"
      contentContainerClassName="sm:flex-1 px-4 pb-8 sm:py-4 sm:p-6"
    >
      <View className="w-full max-w-sm">
        <BackButton />
        <View className="gap-6">
          <Card className="bg-transparent border-0">
            <CardHeader>
              <CardTitle className="text-center text-xl sm:text-left" style={{ color: color.goldText }}>
                Forgot Password
              </CardTitle>
              <CardDescription className="text-center sm:text-left">
                Yeah! It happens. We&apos;ve got your back. Simply supply your registered email below and you&apos;ll be set to
                go.
              </CardDescription>
            </CardHeader>
            <CardContent className="gap-6">
              <View className="gap-6">
                <View className="gap-1.5">
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
                    submitBehavior="submit"
                  />
                  <Text className="text-sm text-destructive">{errors.email ?? ' '}</Text>
                </View>

                <GoldSubmitButton onPress={handleSubmit} isPending={isPending} title="Request Code" />
              </View>
            </CardContent>
          </Card>
        </View>
      </View>
    </Screen>
  );
};

export default ForgotPassword;
