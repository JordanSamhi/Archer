# Research Question 3.b

This folder hosts the necessary files for reproducing our research question 3.b (cf. Section 5.3.2). 

In particular, the following files and folders are included:

- `datasets` folder: contains the dataset used for this experiment
- `results` folder: contains the results of the study with scripts to extract data presented in the paper
- `archer.jar`: the Archer executable used for this experiment
- `run.sh`: a bash script that launches archer with appropriate options
- `run.py`: a python script that retrieves hashes (from our datasets) from a redis server and triggers the execution of archer before sending the results to a redis server.

Please make sure to have the correct environment set up for running the archer.jar, redis server and also review the instructions for running the experiments and extracting the results before proceeding.
