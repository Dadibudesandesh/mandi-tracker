package com.manditracker.api.ingestion;
public record ImportResult(int fetched, int inserted, int updated, int skippedUnmatchedCrop) {}
