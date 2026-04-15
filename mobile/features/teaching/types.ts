export const TEACHING_TYPE = {
  SUNDAY_SERVICE: 'SUNDAY_SERVICE',
  EXTERNAL_MINISTRATION: 'EXTERNAL_MINISTRATION',
  CONFERENCE: 'CONFERENCE',
  SPECIAL_SERVICE: 'SPECIAL_SERVICE',
} as const;

export type TeachingType = (typeof TEACHING_TYPE)[keyof typeof TEACHING_TYPE];

export type Teaching = {
  id: number;
  title: string;
  message: string;
  scripturalReferences: string;
  summary: string;
  audioUrl: string;
  videoUrl: string;
  thumbnailUrl: string;
  teachingType: TeachingType;
  tags: string;
  seriesPart: number;
  taughtAt: string;
  createdAt: string;
  updatedAt: string;
};
