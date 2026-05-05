import { useNavigate } from 'react-router-dom'
import { Tile } from '../components/Tile'
import './HomeView.css'

export function PlayerView() {
  const navigate = useNavigate()

  return (
    <div className="home-view">
      <h1>Rules Engine</h1>
      <div className="tile-grid">
        <Tile
          id="character-editor"
          name="Charactereditor"
          description="Erstellt einen Character."
          onClick={() => navigate('/character-editor')}
        />
      </div>
    </div>
  )
}
