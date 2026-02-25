import { Button } from '@/components/reusables/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/reusables/ui/card';
import { Input } from '@/components/reusables/ui/input';
import { Text } from '@/components/reusables/ui/text';
import GoldGradient from '@/components/ui/gold-gradient';
import { useRequestOtp, useVerifyEmail } from '@/features/auth/hook';
import { verifyEmailSchema, type VerifyEmailSchema } from '@/features/auth/schema';
import { useAuthStore } from '@/features/auth/store';
import useForm from '@/hooks/use-app-form';
import { useRouter } from 'expo-router';
import { useEffect, useState } from 'react';
import { ActivityIndicator, KeyboardAvoidingView, Platform, Pressable, ScrollView, View } from 'react-native';

const VerifyEmail = () => {
  const router = useRouter();
  const { credentials } = useAuthStore();
  const { mutate: verifyEmail, isPending } = useVerifyEmail();
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

  useEffect(() => {
    if (!credentials?.email) {
      router.replace('/register');
    }
  }, [credentials?.email, router]);

  useEffect(() => {
    startRequestOtpCountdown();
  }, []);
  useEffect(() => {
    if (data?.success) {
      startRequestOtpCountdown();
    }
  }, [data?.success]);

  const startRequestOtpCountdown = () => {
    setRequestOtpCountdown(60);
    const requestOtpInterval = setInterval(() => {
      if (isRequestingOtp) {
        return;
      }

      if (requestOtpCountdown === 0) {
        clearInterval(requestOtpInterval);
      } else {
        setRequestOtpCountdown((prev) => prev - 1);
      }
    }, 1000);
    return () => clearInterval(requestOtpInterval);
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
                      className="bg-transparent max-w-32"
                      onPress={handleSubmit}
                      disabled={isPending || isRequestingOtp || requestOtpCountdown !== 0}
                    >
                      {isPending ? <ActivityIndicator color={'#ffffff'} /> : <Text>Verify Email</Text>}
                    </Button>
                  </GoldGradient>
                </View>
              </CardContent>
            </Card>
          </View>
        </View>
      </ScrollView>
    </KeyboardAvoidingView>
  );
};

export default VerifyEmail;
