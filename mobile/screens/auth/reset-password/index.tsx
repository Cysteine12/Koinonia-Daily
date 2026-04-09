import { Screen } from '@/components/core';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/reusables/ui/card';
import { Input } from '@/components/reusables/ui/input';
import { Label } from '@/components/reusables/ui/label';
import { Text } from '@/components/reusables/ui/text';
import BackButton from '@/components/ui/back-button';
import { useLogin, useResetPassword } from '@/features/auth/hook';
import { resetPasswordSchema, type ResetPasswordSchema } from '@/features/auth/schema';
import useForm from '@/hooks/use-app-form';
import { useAppTheme } from '@/hooks/use-app-theme';
import { router, useLocalSearchParams } from 'expo-router';
import React, { useEffect, useRef, useState } from 'react';
import type { TextInput } from 'react-native';
import { View } from 'react-native';
import ConfirmationBottomSheet from '../components/confirmation-bottom-sheet';
import GoldSubmitButton from '../components/gold-submit-button';

const ResetPassword = () => {
  const { mutate: login, isPending: isLoginPending } = useLogin();
  const { color } = useAppTheme();
  const [isModalVisible, setModalVisible] = useState(false);
  const { email } = useLocalSearchParams<{ email?: string }>();
  const { mutate: resetPassword, isPending, data } = useResetPassword();
  const { form, errors, handleChange, handleSubmit } = useForm<ResetPasswordSchema>({
    data: {
      email: email ?? '',
      password: '',
      otp: '',
    },
    schema: resetPasswordSchema,
    onSubmit: (data) => resetPassword(data),
  });

  const otpRef = useRef<TextInput>(null);

  useEffect(() => {
    if (data?.success) {
      setModalVisible(true);
    }
  }, [data]);

  const handleCompleteModal = () => {
    if (isLoginPending) return;
    if (!form.email || !form.password) {
      router.replace('/login');
      return;
    }
    login(
      { email: form.email, password: form.password },
      {
        onError: () => router.replace('/login'),
      }
    );
  };

  useEffect(() => {
    if (!email) {
      router.replace('/forgot-password');
    }
  }, [email]);

  if (!email) {
    return null;
  }

  return (
    <>
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
                  Reset Your Password
                </CardTitle>
                <CardDescription className="text-center sm:text-left">
                  Enter the One-Time Password sent to your email ({email}) and set your new account password
                </CardDescription>
              </CardHeader>
              <CardContent className="gap-6">
                <View className="gap-1.5">
                  <View className="gap-1">
                    <Label htmlFor="password">New Password</Label>
                    <Input
                      id="password"
                      secureTextEntry
                      returnKeyType="next"
                      editable={!isPending}
                      value={form.password}
                      onChangeText={(text) => handleChange('password', text)}
                      onSubmitEditing={() => otpRef.current?.focus()}
                    />
                    <Text className="text-sm text-destructive">{errors.password ?? ' '}</Text>
                  </View>

                  <View className="gap-1.5">
                    <Label htmlFor="otp">OTP Code</Label>
                    <Input
                      id="otp"
                      placeholder="Enter 6-digit code"
                      keyboardType="number-pad"
                      editable={!isPending}
                      value={form.otp}
                      onChangeText={(text) => handleChange('otp', text)}
                      returnKeyType="done"
                      submitBehavior="submit"
                      maxLength={6}
                      ref={otpRef}
                    />
                    <Text className="text-sm text-destructive">{errors.otp ?? ' '}</Text>
                  </View>

                  <GoldSubmitButton onPress={handleSubmit} isPending={isPending} title="Reset Password" />
                </View>
              </CardContent>
            </Card>
          </View>
        </View>
      </Screen>

      <ConfirmationBottomSheet
        visible={isModalVisible}
        onClose={handleCompleteModal}
        title="Password reset successfully!"
        isPending={isLoginPending}
      />
    </>
  );
};

export default ResetPassword;
