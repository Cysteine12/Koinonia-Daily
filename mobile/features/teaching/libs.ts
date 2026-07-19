import { TEACHING_TYPE_TAGS, type TeachingType } from './types';

export const getTeachingTypeColor = (type: TeachingType) => {
  return TEACHING_TYPE_TAGS.find((type_tag) => type_tag.type === type)?.color;
};

export const getTeachingTypeText = (type: TeachingType) => {
  return TEACHING_TYPE_TAGS.find((type_tag) => type_tag.type === type)?.text;
};
