function removeAccents(str) {
  return str.normalize('NFD').replace(/[\u0300-\u036f]/g, '');
}

export function normalizeString(str) {
  return removeAccents(str).toLowerCase().trim();
}
