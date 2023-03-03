package uz.pdp.spring_boot_security_web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.spring_boot_security_web.entity.TaskEntity;
import uz.pdp.spring_boot_security_web.entity.TestCaseEntity;
import uz.pdp.spring_boot_security_web.common.exception.RecordNotFountException;
import uz.pdp.spring_boot_security_web.model.dto.TestCaseDto;
import uz.pdp.spring_boot_security_web.repository.TaskRepository;
import uz.pdp.spring_boot_security_web.repository.TestCaseRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestCaseService implements BaseService<TestCaseEntity, TestCaseDto> {

    private final TestCaseRepository testCaseRepository;
    private final TaskRepository taskRepository;

    @Override
    public List<TestCaseEntity> getList() {
        return testCaseRepository.findAll();
    }

    public List<TestCaseEntity>getTestCaseListByTaskId(int id){

        return  testCaseRepository.findByQuestionId(id);
    }


    @Override
    public TestCaseEntity getById(int id) {
        Optional<TestCaseEntity> byId = testCaseRepository.findById(id);
        if (byId.isEmpty()) {
            throw new IllegalArgumentException("Test case not found");
        }
        return byId.get();
    }

    @Override
    public boolean delete(int id) {
        Optional<TestCaseEntity> byId = testCaseRepository.findById(id);
        if (byId.isEmpty()) {
            throw new RecordNotFountException("Test case not found");
        }
        testCaseRepository.deleteById(id);
        return true;
    }

    @Override
    public TestCaseEntity add(TestCaseDto testCaseDto) {
        TestCaseEntity test = TestCaseEntity.builder()
                .firstParam(testCaseDto.getFirstParam())
                .secondParam(testCaseDto.getSecondParam())
                .testCaseName(testCaseDto.getTestCaseName())
                .question(taskRepository.findByIdIn(Collections.singleton(testCaseDto.getTaskId())))
                //.question(taskRepository.findByName(testCaseDto.getQuestionName()))
                .build();
        String result=null;
        if(testCaseDto.getResult().startsWith("\"")&&testCaseDto.getResult().endsWith("\"")){
            result = testCaseDto.getResult().substring(1,testCaseDto.getResult().length()-2);
            test.setResult(result);
        }else {
            test.setResult(testCaseDto.getResult());
        }
        return testCaseRepository.save(test);
    }

    public List<TestCaseEntity> testCasesOfQuestion(Integer id){

        return testCaseRepository.findAllByQuestionId(id);
    }

    public List<String> quantityOfSuccessfulTestCases(List<String> list){
        return list.stream().filter(s -> s.contains("✅")).toList();
    }

    public void update(int id, TestCaseDto testCaseDto) {
        Optional<TestCaseEntity> optionalTestCase = testCaseRepository.findById(id);
        if (optionalTestCase.isEmpty()){
            throw new IllegalArgumentException("Test case not found");
        }
        TestCaseEntity testCaseEntity = optionalTestCase.get();

        testCaseEntity.setTestCaseName(testCaseDto.getTestCaseName());
        testCaseEntity.setFirstParam(testCaseDto.getFirstParam());
        testCaseEntity.setSecondParam(testCaseDto.getSecondParam());
        testCaseEntity.setResult(testCaseDto.getResult());
        testCaseRepository.save(testCaseEntity);

    }
}
