import uniqWith from "lodash/uniqWith";

import { FormError, FormErrorMap } from "MyTypes";

export const normalizeErrors = (errors: FormError[]): FormErrorMap =>
  errors.reduce((result, error) => {
    result[error.property] = error;
    return result;
  }, {} as FormErrorMap);

export const fillErrors = (fields: string[], error: string): FormErrorMap =>
  fields.reduce((result, field) => {
    result[field] = {
      message: error,
      property: field,
      value: "",
    };
    return result;
  }, {} as FormErrorMap);

export const getRequiredErrors = (trim = true) => {
  const required = { required: true, message: "Required" };
  const whitespace = {
    whitespace: true,
    message: "Required",
  };
  return trim ? [required, whitespace] : [required];
};

export const getMinError = (min: number) => ({
  min,
  message: `Should be at least ${min} characters long`,
});

export const getMaxError = (max: number) => ({
  max,
  message: `Must be no more than ${max} characters`,
});

export const getUrlError = () => ({
  pattern: /^[a-z0-9]+(?:[-_][a-z0-9]+)*$/,
  message: "Not valid",
});

export const getUniqError = (fields: any[], fieldKey: string) => ({
  validator() {
    const uniqValues = uniqWith(fields, (prev: any, curr: any) => {
      return prev[fieldKey] === curr[fieldKey];
    });

    if (uniqValues.length === fields.length) {
      return Promise.resolve();
    }

    return Promise.reject("The value must be unique");
  },
});
