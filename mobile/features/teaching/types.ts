export const TEACHING_TYPE = {
  SUNDAY_SERVICE: 'SUNDAY_SERVICE',
  EXTERNAL_MINISTRATION: 'EXTERNAL_MINISTRATION',
  CONFERENCE: 'CONFERENCE',
  SPECIAL_SERVICE: 'SPECIAL_SERVICE',
} as const;

export const TEACHING_TYPE_TAGS = [
  { text: 'All', type: 'ALL', color: '' },
  { text: 'Sunday Service', type: 'SUNDAY_SERVICE', color: '#22C55E' },
  { text: 'Conference', type: 'CONFERENCE', color: '#3B82F6' },
  { text: 'External Ministration', type: 'EXTERNAL_MINISTRATION', color: '#8B5CF6' },
  { text: 'Special Service', type: 'SPECIAL_SERVICE', color: '#F59E0B' },
] as const satisfies readonly { text: string; type: TeachingType | 'ALL'; color: string }[];

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
