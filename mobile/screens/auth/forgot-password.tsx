import { Button } from '@/components/reusables/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/reusables/ui/card';
import { Input } from '@/components/reusables/ui/input';
import { Label } from '@/components/reusables/ui/label';
import GoldGradient from '@/components/ui/gold-gradient';
import { useForgotPassword } from '@/features/auth/hook';
import { forgotPasswordSchema, type ForgotPasswordSchema } from '@/features/auth/schema';
import useForm from '@/hooks/use-app-form';
import { useLocalSearchParams } from 'expo-router';
import React from 'react';
import {
  ActivityIndicator,
  KeyboardAvoidingView,
  Platform,
  ScrollView,
  Text,
  useColorScheme,
  View,
} from 'react-native';

const ForgotPassword = () => {
  const colorScheme = useColorScheme();
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
                <CardTitle className="text-center text-gold-text text-xl sm:text-left">Forgot Password</CardTitle>
                <CardDescription className="text-center sm:text-left">
                  Yeah! It happens. We&apos;ve got your back. Simply supply your registered email below and you&apos;ll
                  be set to go.
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
                      onChangeText={(text) => handleChange('email', text)}
                      returnKeyType="next"
                      submitBehavior="submit"
                    />
                    {errors?.email && <Text className="text-sm text-destructive">{errors?.email}</Text>}
                  </View>
                  <GoldGradient>
                    <Button className="bg-transparent w-full" onPress={handleSubmit} disabled={isPending}>
                      {isPending ? (
                        <ActivityIndicator color={colorScheme === 'dark' ? '#000000' : '#ffffff'} />
                      ) : (
                        <Text>Continue</Text>
                      )}
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

export default ForgotPassword;
