export const filterOptions = (input: string, option: any) =>
  option!.children.toLowerCase().indexOf(input.toLowerCase()) >= 0;
