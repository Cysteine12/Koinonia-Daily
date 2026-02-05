import { useState } from 'react';
import type { ZodObject } from 'zod';

interface UseAppFormProps<T> {
  data: T;
  schema: ZodObject;
  onSubmit: () => void;
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
      result.error.issues.forEach((issue) => {
        if (issue.path[0]) {
          const key = issue.path[0].toString() as keyof T;
          setErrors((prev) => ({
            ...prev,
            [key]: issue.message,
          }));
        }
      });
      return;
    }
    setErrors({});

    onSubmit();
  };

  return { form, errors, handleChange, handleSubmit };
};

export default useForm;
