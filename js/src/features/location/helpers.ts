export function convertFileToBase64(file: Blob): Promise<string> {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);

    reader.onload = () =>
      resolve(
        (reader.result as string).replace("data:", "").replace(/^.+,/, ""),
      );
    reader.onerror = () => reject();
  });
}
