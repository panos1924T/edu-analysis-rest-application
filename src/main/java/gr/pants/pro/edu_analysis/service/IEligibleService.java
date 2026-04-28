package gr.pants.pro.edu_analysis.service;


import gr.pants.pro.edu_analysis.dto.JobStatusDTO;

public interface IEligibleService {
    void generateReport(String jobId);
    JobStatusDTO getJobStatus(String jobId);
}
