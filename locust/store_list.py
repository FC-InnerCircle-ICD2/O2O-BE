from locust import HttpUser, task


class StoreApiUser(HttpUser):

    @task
    def get_store_list(self):
        params = {
            "latitude": 37.5579907597889,
            "longitude": 126.921149447336,
            "page": 1,   # int 값으로 변경
            "size": 10   # int 값으로 변경
        }
        header = {
            "X-User-Lat": str(37.5579907597889),  # float -> string 변환
            "X-User-Lng": str(126.921149447336)   # float -> string 변환
        }
        self.client.get("/api/v1/stores/list", params=params, headers=header)
