
import apiClient from "./client";

export async function getCrops() {
  const response = await apiClient.get("/crops");
  return response.data;
}
