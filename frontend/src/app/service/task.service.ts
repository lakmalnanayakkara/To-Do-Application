import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface StandardResponse {
  code: number;
  message: string;
  date: object;
}

export interface TaskDetails {
  taskTitle: string;
  taskDate: string;
  taskTime: string;
  description: string;
}

@Injectable({
  providedIn: 'root',
})
export class TaskService {
  private apiUrl = '/api';
  constructor(private http: HttpClient) {}

  addTask(taskDetails: TaskDetails): Observable<any> {
    return this.http.post(`${this.apiUrl}/add-task`, taskDetails);
  }

  getTasks(): Observable<any> {
    return this.http.get(`${this.apiUrl}/get-tasks`);
  }

  deleteTask(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete-task?id=${id}`);
  }

  updateTask(id: number, taskDetails: TaskDetails): Observable<any> {
    return this.http.put(`${this.apiUrl}/update-task?id=${id}`, taskDetails);
  }

  getTask(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/get-task?id=${id}`);
  }
}
