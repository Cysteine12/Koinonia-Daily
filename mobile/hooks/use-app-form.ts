import { useState } from 'react';
import type { ZodObject } from 'zod';

interface UseAppFormProps<T extends Record<string, any>> {
  data: T;
  schema: ZodObject;
  onSubmit: (data: T) => void;
}

const useForm = <T extends Record<string, any>>({ data, schema, onSubmit }: UseAppFormProps<T>) => {
  const [form, setForm] = useState(data);
  const [errors, setErrors] = useState<Partial<Record<keyof T, string>>>({});

  const handleChange = (key: keyof T, value: any) => {
    setForm((prevForm) => ({
      ...prevForm,
      [key]: value,
    }));
  };

  const handleSubmit = () => {
    const result = schema.safeParse(form);

    if (!result.success) {
      const newErrors: Partial<Record<keyof T, string>> = {};
      result.error.issues.forEach((issue) => {
        if (issue.path[0]) {
          const key = issue.path[0].toString() as keyof T;
          if (!newErrors[key]) {
            newErrors[key] = issue.message;
          }
        }
      });
      setErrors(newErrors);
      return;
    }
    setErrors({});

    onSubmit(result.data as T);
  };

  return { form, errors, handleChange, handleSubmit };
};

export default useForm;
