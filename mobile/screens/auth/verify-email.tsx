import { Button } from '@/components/reusables/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/reusables/ui/card';
import { Input } from '@/components/reusables/ui/input';
import { Text } from '@/components/reusables/ui/text';
import { ThemedText } from '@/components/themed-text';
import BottomSheet from '@/components/ui/bottom-sheet';
import GoldGradient from '@/components/ui/gold-gradient';
import { IconSymbol } from '@/components/ui/icon-symbol';
import { Colors } from '@/constants/theme';
import { useLogin, useRequestOtp, useVerifyEmail } from '@/features/auth/hook';
import { verifyEmailSchema, type VerifyEmailSchema } from '@/features/auth/schema';
import { useAuthStore } from '@/features/auth/store';
import useForm from '@/hooks/use-app-form';
import { useAppTheme } from '@/hooks/use-app-theme';
import { useRouter } from 'expo-router';
import { useEffect, useState } from 'react';
import { ActivityIndicator, KeyboardAvoidingView, Platform, Pressable, ScrollView, View } from 'react-native';

const VerifyEmail = () => {
  const { color } = useAppTheme();
  const router = useRouter();
  const { mutate: login, isPending: isLoginPending } = useLogin();
  const [isModalOpen, setModalOpen] = useState(false);
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
      setModalOpen(true);
    }
  }, [verifyEmailData]);

  const handleCompleteModal = () => {
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
                <CardTitle className="text-center text-gold-text text-xl sm:text-left">Verify your email</CardTitle>
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
                  <GoldGradient className="flex-1 self-center">
                    <Button
                      className="bg-transparent max-w-32 font-semibold"
                      onPress={handleSubmit}
                      disabled={isPending || isRequestingOtp}
                    >
                      {isPending ? <ActivityIndicator /> : <Text className="text-black">Verify Email</Text>}
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
          <IconSymbol name="checkmark.circle" size={40} color={color.background} />
        </GoldGradient>

        <ThemedText className="text-center text-2xl">Email verified successfully!</ThemedText>

        <Button
          className="mt-8 self-center bg-transparent w-full border border-gold"
          onPress={handleCompleteModal}
          disabled={isLoginPending}
        >
          {isLoginPending ? (
            <ActivityIndicator color={Colors.goldIcon} />
          ) : (
            <Text>
              <Text className="text-gold-text font-semibold">Continue</Text>
              <IconSymbol name="arrow.forward" size={16} color={Colors.goldIcon} />
            </Text>
          )}
        </Button>
      </BottomSheet>
    </KeyboardAvoidingView>
  );
};

export default VerifyEmail;
