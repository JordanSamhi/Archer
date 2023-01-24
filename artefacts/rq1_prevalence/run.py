import requests
import sys
import math
import redis
import time
import subprocess

REDIS = redis.Redis(host="YOUR_REDIS_SERVER", port=6379, db=0,
                    password="YOUR_REDIS_PASSWORD")
URL = "ANDROZOO_URL"
APIKEY = "ANDROZOO_API_KEY"
REDIS_ERRORS = "YOUR_REDIS_LIST"
REDIS_SUCCESS = "YOUR_REDIS_LIST"
CMD = "bash ./run.sh"


if __name__ == "__main__":
    REDIS_POP = "YOUR_REDIS_LIST"
    while True:
        pop = REDIS.spop(REDIS_POP)
        print(f"Analyzing: {pop}")
        if pop is None:
            time.sleep(30)
            continue
        try:
            sha = pop.decode("utf8")
            if REDIS.sismember(REDIS_SUCCESS, sha):
                continue
            r = requests.get(URL, params={"apikey": APIKEY, "sha256": sha},
                             stream=True)
            filename = f"./apps/{sha}.apk"
            with open(filename,'bw') as f:
                f.write(r.content)
            process = subprocess.Popen(f"{CMD} {filename}", stdout=subprocess.PIPE, shell=True)
            out, err = process.communicate()
            out = out.decode("utf8").rstrip()
            print(out)
            REDIS.sadd(REDIS_SUCCESS, sha)
        except Exception as e:
            print("[!] An exception occured with: {}".format(sha))
            print("Exception: {}".format(e))
            REDIS.sadd(REDIS_ERRORS, sha)
            continue
