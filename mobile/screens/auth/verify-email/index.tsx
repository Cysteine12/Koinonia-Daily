import { Screen } from '@/components/core';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/reusables/ui/card';
import { Input } from '@/components/reusables/ui/input';
import { Text } from '@/components/reusables/ui/text';
import BackButton from '@/components/ui/back-button';
import { useLogin, useRequestOtp, useVerifyEmail } from '@/features/auth/hook';
import { verifyEmailSchema, type VerifyEmailSchema } from '@/features/auth/schema';
import { useAuthStore } from '@/features/auth/store';
import useForm from '@/hooks/use-app-form';
import { useAppTheme } from '@/hooks/use-app-theme';
import { useRouter } from 'expo-router';
import { useEffect, useState } from 'react';
import { ActivityIndicator, Pressable, View } from 'react-native';
import ConfirmationBottomSheet from '../components/confirmation-bottom-sheet';
import GoldSubmitButton from '../components/gold-submit-button';

const VerifyEmail = () => {
  const { color } = useAppTheme();
  const router = useRouter();
  const { mutate: login, isPending: isLoginPending } = useLogin();
  const [isModalVisible, setModalVisible] = useState(false);
  const { credentials } = useAuthStore();
  const { mutate: verifyEmail, isPending, data: verifyEmailData } = useVerifyEmail();
  const { mutate: requestOtp, isPending: isRequestingOtp, data } = useRequestOtp();
  const [requestOtpCountdown, setRequestOtpCountdown] = useState(60);
  const { form, errors, handleChange, handleSubmit } = useForm<VerifyEmailSchema>({
    data: {
      email: credentials?.email || '',
      otp: '',
    },
    schema: verifyEmailSchema,
    onSubmit: (data) => verifyEmail(data),
  });

  const resetRequestOtpCountdown = () => setRequestOtpCountdown(60);

  useEffect(() => {
    resetRequestOtpCountdown();
  }, []);

  useEffect(() => {
    if (data?.success) resetRequestOtpCountdown();
  }, [data]);

  useEffect(() => {
    if (isRequestingOtp || requestOtpCountdown <= 0) return;

    const id = setTimeout(() => {
      setRequestOtpCountdown((prev) => (prev <= 1 ? 0 : prev - 1));
    }, 1000);

    return () => clearTimeout(id);
  }, [isRequestingOtp, requestOtpCountdown]);

  useEffect(() => {
    if (verifyEmailData?.success) {
      setModalVisible(true);
    }
  }, [verifyEmailData]);

  const handleCompleteModal = () => {
    if (isLoginPending) return;
    if (!credentials?.email || !credentials?.password) {
      router.replace('/login');
      return;
    }
    login(
      { email: credentials.email, password: credentials.password },
      {
        onError: () => router.replace('/login'),
      }
    );
  };

  useEffect(() => {
    if (!credentials?.email) {
      router.replace('/register');
    }
  }, [credentials?.email, router]);

  if (!credentials?.email) {
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
                <CardTitle className="text-center text-gold-text text-xl sm:text-left" style={{ color: color.goldText }}>
                  Verify your email
                </CardTitle>
                <CardDescription className="text-center sm:text-left">
                  Please enter the 6-digit code sent to your email to verify your account.
                </CardDescription>
              </CardHeader>
              <CardContent className="gap-6">
                <View className="gap-6">
                  <View className="flex-1 my-2">
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
                      className="h-16 max-w-48 self-center text-center text-2xl tracking-widest"
                    />
                    {errors?.otp && <Text className="text-sm self-center text-destructive">{errors?.otp}</Text>}
                    <Pressable
                      onPress={() => requestOtp()}
                      className="mt-4 self-center"
                      disabled={isPending || isRequestingOtp || requestOtpCountdown !== 0}
                    >
                      <Text className="text-sm text-gold-text">
                        Resend code
                        {isRequestingOtp ? (
                          <ActivityIndicator className="flex ml-2" size="small" color="#9d7c1b" />
                        ) : (
                          ![0, 60].includes(requestOtpCountdown) && `(${requestOtpCountdown})`
                        )}
                      </Text>
                    </Pressable>
                  </View>

                  <GoldSubmitButton
                    onPress={handleSubmit}
                    isPending={isPending}
                    title="Verify Email"
                    disabled={isPending || isRequestingOtp}
                  />
                </View>
              </CardContent>
            </Card>
          </View>
        </View>
      </Screen>

      <ConfirmationBottomSheet
        visible={isModalVisible}
        onClose={handleCompleteModal}
        title="Email verified successfully!"
        isPending={isLoginPending}
      />
    </>
  );
};

export default VerifyEmail;
