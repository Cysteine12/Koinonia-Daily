import { Button } from '@/components/reusables/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/reusables/ui/card';
import { Input } from '@/components/reusables/ui/input';
import { Label } from '@/components/reusables/ui/label';
import { Text } from '@/components/reusables/ui/text';
import { ThemedText } from '@/components/themed-text';
import BottomSheet from '@/components/ui/bottom-sheet';
import GoldGradient from '@/components/ui/gold-gradient';
import { IconSymbol } from '@/components/ui/icon-symbol';
import { Colors } from '@/constants/theme';
import { useLogin, useResetPassword } from '@/features/auth/hook';
import { resetPasswordSchema, type ResetPasswordSchema } from '@/features/auth/schema';
import useForm from '@/hooks/use-app-form';
import { router, useLocalSearchParams } from 'expo-router';
import React, { useEffect, useRef, useState } from 'react';
import type { TextInput } from 'react-native';
import { ActivityIndicator, KeyboardAvoidingView, Platform, ScrollView, useColorScheme, View } from 'react-native';

const ResetPassword = () => {
  const { mutate: login } = useLogin();
  const colorScheme = useColorScheme();
  const [isModalOpen, setModalOpen] = useState(false);
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
      setModalOpen(true);
    }
  }, [data]);

  const handleCompleteModal = () => {
    if (!form.email || !form.password) {
      router.replace('/login');
      return;
    }
    login({ email: form.email, password: form.password });
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
                <CardTitle className="text-center text-gold-text text-xl sm:text-left">Reset Your Password</CardTitle>
                <CardDescription className="text-center sm:text-left">
                  Enter the One-Time Password sent to your email ({email}) and set your new account password
                </CardDescription>
              </CardHeader>
              <CardContent className="gap-6">
                <View className="gap-6">
                  <View className="gap-1.5">
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
                    {errors?.password && <Text className="text-sm text-destructive">{errors?.password}</Text>}
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
                    {errors?.otp && <Text className="text-sm text-destructive">{errors?.otp}</Text>}
                  </View>
                  <GoldGradient>
                    <Button className="bg-transparent w-full font-semibold" onPress={handleSubmit} disabled={isPending}>
                      {isPending ? (
                        <ActivityIndicator color={colorScheme === 'dark' ? '#000000' : '#ffffff'} />
                      ) : (
                        <Text>Reset Password</Text>
                      )}
                    </Button>
                  </GoldGradient>
                </View>
              </CardContent>
            </Card>
          </View>
        </View>
      </ScrollView>
      <BottomSheet isOpen={isModalOpen} onClose={handleCompleteModal}>
        <GoldGradient className="w-16 h-16 rounded-full items-center justify-center self-center mb-8">
          <IconSymbol name="checkmark.circle" size={40} color={colorScheme === 'dark' ? '#000000' : '#ffffff'} />
        </GoldGradient>
        <ThemedText className="text-center text-2xl">Password reset successfully!</ThemedText>
        <Button
          className="mt-8 self-center bg-transparent w-full border border-gold items-center"
          onPress={handleCompleteModal}
        >
          <Text className="font-semibold text-gold-text">Continue</Text>
          <IconSymbol name="arrow.forward" size={16} className="text-gold-text" color={Colors.goldIcon} />
        </Button>
      </BottomSheet>
    </KeyboardAvoidingView>
  );
};

export default ResetPassword;
