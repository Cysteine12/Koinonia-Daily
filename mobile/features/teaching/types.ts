export const TeachingType = {
  SUNDAY_SERVICE: 'SUNDAY_SERVICE',
  EXTERNAL_MINISTRATION: 'EXTERNAL_MINISTRATION',
  CONFERENCE: 'CONFERENCE',
  SPECIAL_SERVICE: 'SPECIAL_SERVICE',
} as const;

export type Teaching = {
  id: number;
  title: string;
  message: string;
  scripturalReferences: string;
  summary: string;
  audioUrl: string;
  videoUrl: string;
  thumbnailUrl: string;
  teachingType: typeof TeachingType;
  tags: string;
  seriesPart: number;
  taughtAt: string;
  createdAt: string;
  updatedAt: string;
};
