import { TestBed } from '@angular/core/testing';
import { HttpClient } from '@angular/common/http';
import { TaskService, TaskDetails } from './task.service';
import { of, throwError } from 'rxjs';

describe('TaskService', () => {
  let service: TaskService;
  let httpClient: jest.Mocked<HttpClient>;

  const mockTask: TaskDetails = {
    taskTitle: 'Test Task',
    taskDate: '2024-05-20',
    taskTime: '10:00',
    description: 'Test Description',
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        TaskService,
        {
          provide: HttpClient,
          useValue: {
            post: jest.fn(),
            get: jest.fn(),
            delete: jest.fn(),
            put: jest.fn(),
          },
        },
      ],
    });

    service = TestBed.inject(TaskService);
    httpClient = TestBed.inject(HttpClient) as jest.Mocked<HttpClient>;
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('addTask', () => {
    it('should send POST request to correct endpoint', () => {
      const mockResponse = { code: 201, message: 'SUCCESS', data: {} };
      httpClient.post.mockReturnValue(of(mockResponse));

      service.addTask(mockTask).subscribe((response) => {
        expect(response).toEqual(mockResponse);
      });

      expect(httpClient.post).toHaveBeenCalledWith(
        'http://localhost:8081/api/v1/task/add-task',
        mockTask
      );
    });

    it('should handle add task errors', () => {
      const errorResponse = new Error('Server error');
      httpClient.post.mockReturnValue(throwError(() => errorResponse));

      service.addTask(mockTask).subscribe({
        error: (err) => expect(err).toBe(errorResponse),
      });
    });
  });

  describe('getTasks', () => {
    it('should send GET request to correct endpoint', () => {
      const mockResponse = { code: 200, message: 'SUCCESS', data: [mockTask] };
      httpClient.get.mockReturnValue(of(mockResponse));

      service.getTasks().subscribe((response) => {
        expect(response).toEqual(mockResponse);
      });

      expect(httpClient.get).toHaveBeenCalledWith(
        'http://localhost:8081/api/v1/task/get-tasks'
      );
    });

    it('should handle get tasks errors', () => {
      const errorResponse = new Error('Server error');
      httpClient.get.mockReturnValue(throwError(() => errorResponse));

      service.getTasks().subscribe({
        error: (err) => expect(err).toBe(errorResponse),
      });
    });
  });

  describe('deleteTask', () => {
    it('should send DELETE request with correct ID', () => {
      const taskId = 123;
      const mockResponse = { code: 200, message: 'SUCCESS', data: {} };
      httpClient.delete.mockReturnValue(of(mockResponse));

      service.deleteTask(taskId).subscribe((response) => {
        expect(response).toEqual(mockResponse);
      });

      expect(httpClient.delete).toHaveBeenCalledWith(
        `http://localhost:8081/api/v1/task/delete-task?id=${taskId}`
      );
    });

    it('should handle delete task errors', () => {
      const taskId = 123;
      const errorResponse = new Error('Server error');
      httpClient.delete.mockReturnValue(throwError(() => errorResponse));

      service.deleteTask(taskId).subscribe({
        error: (err) => expect(err).toBe(errorResponse),
      });
    });
  });

  describe('updateTask', () => {
    it('should send PUT request with correct ID and data', () => {
      const taskId = 123;
      const mockResponse = { code: 200, message: 'SUCCESS', data: {} };
      httpClient.put.mockReturnValue(of(mockResponse));

      service.updateTask(taskId, mockTask).subscribe((response) => {
        expect(response).toEqual(mockResponse);
      });

      expect(httpClient.put).toHaveBeenCalledWith(
        `http://localhost:8081/api/v1/task/update-task?id=${taskId}`,
        mockTask
      );
    });

    it('should handle update task errors', () => {
      const taskId = 123;
      const errorResponse = new Error('Server error');
      httpClient.put.mockReturnValue(throwError(() => errorResponse));

      service.updateTask(taskId, mockTask).subscribe({
        error: (err) => expect(err).toBe(errorResponse),
      });
    });
  });

  describe('getTask', () => {
    it('should send GET request with correct ID', () => {
      const taskId = 123;
      const mockResponse = { code: 200, message: 'SUCCESS', data: mockTask };
      httpClient.get.mockReturnValue(of(mockResponse));

      service.getTask(taskId).subscribe((response) => {
        expect(response).toEqual(mockResponse);
      });

      expect(httpClient.get).toHaveBeenCalledWith(
        `http://localhost:8081/api/v1/task/get-task?id=${taskId}`
      );
    });

    it('should handle get task errors', () => {
      const taskId = 123;
      const errorResponse = new Error('Server error');
      httpClient.get.mockReturnValue(throwError(() => errorResponse));

      service.getTask(taskId).subscribe({
        error: (err) => expect(err).toBe(errorResponse),
      });
    });
  });
});
