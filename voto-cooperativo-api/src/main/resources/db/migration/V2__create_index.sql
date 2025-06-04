-- Add index for performance optimization

-- Index on agenda_id in the vote table for faster lookups of votes per agenda
CREATE INDEX idx_vote_agenda_id ON vote(agenda_id);