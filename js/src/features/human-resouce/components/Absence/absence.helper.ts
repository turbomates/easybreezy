import { AbsenceForm, Absence } from "HumanResourceModels";
import parseISO from "date-fns/esm/parseISO";
import format from "date-fns/format";
import { BACKEND_DATE_FORMAT } from "../../../../constants";

export const deserializeForm = (form: Absence) => {
  const startedAt = parseISO(form.startedAt);
  const endedAt = parseISO(form.endedAt);

  const range = [startedAt, endedAt];

  return {
    range,
    reason: form.reason,
    comment: form.comment,
  };
};

export const serializeForm = (form: any): AbsenceForm => ({
  startedAt: format(form.range[0], BACKEND_DATE_FORMAT),
  endedAt: format(form.range[1], BACKEND_DATE_FORMAT),
  reason: form.reason,
  comment: form.comment,
});
