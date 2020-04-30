import generatePicker from "antd/es/date-picker/generatePicker";
import "antd/es/date-picker/style/index";

import { generateConfig } from "./generateConfig";

const DatePicker = generatePicker<Date>(generateConfig);

export default DatePicker;
