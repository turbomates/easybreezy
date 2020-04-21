import { GenerateConfig } from "rc-picker/es/generate";
import { default as getWeekDay } from "date-fns/esm/getDay";
import getYear from "date-fns/esm/getYear";
import getMonth from "date-fns/esm/getMonth";
import getDate from "date-fns/esm/getDate";
import getHour from "date-fns/esm/getHours";
import getMinute from "date-fns/esm/getMinutes";
import getSecond from "date-fns/esm/getMinutes";
import getWeekYear from "date-fns/esm/getWeekYear";
import addYear from "date-fns/esm/addYears";
import addMonth from "date-fns/esm/addMonths";
import addDate from "date-fns/esm/addDays";
import setYear from "date-fns/esm/setYear";
import setMonth from "date-fns/esm/setMonth";
import setDate from "date-fns/esm/setDate";
import setMinute from "date-fns/esm/setMinutes";
import setHour from "date-fns/esm/setHours";
import setSecond from "date-fns/esm/setSeconds";
import isAfter from "date-fns/esm/isAfter";
import isValidate from "date-fns/esm/isValid";
import eachDay from "date-fns/esm/eachDayOfInterval";
import eachMonth from "date-fns/esm/eachMonthOfInterval";
import startOfWeek from "date-fns/esm/startOfWeek";
import endOfWeek from "date-fns/esm/endOfWeek";
import startOfYear from "date-fns/esm/startOfYear";
import endOfYear from "date-fns/esm/endOfYear";
import format from "date-fns/esm/format";
import parse from "date-fns/esm/parse";
import en from "date-fns/esm/locale/en-US";
import ru from "date-fns/esm/locale/ru";

type localeMapObject = { [key: string]: any };
const localeMap: localeMapObject = { en, ru };
const parseLocale = (locale: string) => localeMap[locale] || en;

export const generateConfig: GenerateConfig<Date> = {
  // get
  getNow: () => new Date(),
  getWeekDay,
  getYear,
  getMonth,
  getDate,
  getHour,
  getMinute,
  getSecond,

  // set
  addYear,
  addMonth,
  addDate,
  setYear,
  setMonth,
  setDate,
  setHour,
  setMinute,
  setSecond,

  // compare
  isAfter,
  isValidate,

  locale: {
    getWeekFirstDay: (locale) => parseLocale(locale).options.weekStartsOn,
    getWeek: (locale, value) =>
      getWeekYear(value, { locale: parseLocale(locale) }),

    format: (locale, date, fmt) => {
      // console.log(fmt);
      return format(date, fmt.replace("YYYY", "yyyy").replace("DD", "dd"), {
        locale: parseLocale(locale),
      });
    },

    parse: (locale, text, formats) => {
      const localeObj = parseLocale(locale);
      for (const format of formats) {
        const fmt = format.replace("YYYY", "yyyy").replace("DD", "dd");
        const date = parse(text, fmt, new Date(), { locale: localeObj });
        if (isValidate(date)) {
          return date;
        }
      }
      return null;
    },

    getShortWeekDays: (locale) => {
      const now = new Date();
      const week = eachDay({ start: startOfWeek(now), end: endOfWeek(now) });
      return week.map((day) =>
        format(day, "dd", { locale: parseLocale(locale) }),
      );
    },

    getShortMonths: (locale) => {
      const now = new Date();
      const year = eachMonth({ start: startOfYear(now), end: endOfYear(now) });
      return year.map((month) =>
        format(month, "MMM", { locale: parseLocale(locale) }),
      );
    },
  },
};
