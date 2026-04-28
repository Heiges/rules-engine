import { useNavigate } from 'react-router-dom'
import './DetailView.css'

export function SpielweltView() {
  const navigate = useNavigate()

  return (
    <div className="detail-view">
      <button className="back-button" onClick={() => navigate('/')}>← Zurück</button>
      <h1>Spielwelt anlegen</h1>
      <p>Hier wird das Anlegen einer neuen Spielwelt implementiert.</p>
    </div>
  )
}
