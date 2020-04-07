const version = process.env.APP_VERSION || 0;
const PREFIX = `MY_APP_v${version}::`;

export function set<T = object>(key: string, value: T): void {
  if (!window.localStorage) {
    return;
  }

  try {
    const serializedValue = JSON.stringify(value);
    window.localStorage.setItem(PREFIX + key, serializedValue);
  } catch (error) {
    throw new Error("store serialization failed");
  }
}

export function get<T = object>(key: string): T | undefined {
  if (!window.localStorage) {
    return;
  }

  try {
    const serializedValue = window.localStorage.getItem(PREFIX + key);
    if (serializedValue == null) {
      return;
    }
    return JSON.parse(serializedValue);
  } catch (error) {
    throw new Error("store deserialization failed");
  }
}

export function remove(key: string) {
  if (!window.localStorage) {
    return;
  }

  try {
    window.localStorage.removeItem(PREFIX + key);
  } catch (error) {
    throw new Error("store deserialization failed");
  }
}
